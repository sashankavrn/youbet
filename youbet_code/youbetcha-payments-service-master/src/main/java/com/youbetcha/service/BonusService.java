package com.youbetcha.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.youbetcha.client.InternalPaymentClient;
import com.youbetcha.exceptions.DepositBonusException;
import com.youbetcha.exceptions.PlayerNotFoundException;
import com.youbetcha.model.BaseDepositBonusRequest;
import com.youbetcha.model.BonusCountry;
import com.youbetcha.model.BonusUser;
import com.youbetcha.model.DepositBonus;
import com.youbetcha.model.DepositBonusRequest;
import com.youbetcha.model.Player;
import com.youbetcha.model.UpdateDepositBonusRequest;
import com.youbetcha.model.payments.dto.DepositBonusDto;
import com.youbetcha.repository.BonusCountryRepository;
import com.youbetcha.repository.BonusUserRepository;
import com.youbetcha.repository.DepositBonusRepository;

@Service
public class BonusService {

    private static final String DEFAULT_COUNTRY_CODE = "XX";

	private static final Logger LOGGER = LoggerFactory.getLogger(BonusService.class);

    Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy, HH:mm:ss").create();

    @Autowired
    private DepositBonusRepository depositBonusRepository;

    @Autowired
    private BonusCountryRepository bonusCountryRepository;

    @Autowired
    private BonusUserRepository bonusUserRepository;

    @Autowired
    private InternalPaymentClient internalClient;
    
    @Autowired @Qualifier("mvcConversionService")
    private ConversionService conversionService;

	public List<DepositBonus> getAllDepositBonuses() {
		
		List<DepositBonus> bonuses = (List<DepositBonus>) depositBonusRepository.findAll();
		for (DepositBonus db : bonuses) {
			db.setActiveCountries(bonusCountryRepository.findBonusCountryByBonusId(db.getId()));
			db.setActiveUsers(bonusUserRepository.findBonusUserByBonusId(db.getId()));
		}
		
		return bonuses;
	}

	public List<DepositBonusDto> getAllDepositBonusesByUser(String email) {
		// Fetch the user from the given internal ID
		Player player = internalClient.getPlayer(email).orElseThrow(() -> new PlayerNotFoundException("Could not find player for email: " + email));
		LOGGER.info("D> Got player: {}", player);
		
		// Get all bonuses relevant for this player based on country and ID
		List<DepositBonus> bonusesByUser = combineDepositBonuses(player);
		
		List<DepositBonusDto> responses = new ArrayList<DepositBonusDto>();
		for (DepositBonus db : bonusesByUser) {
			if (db.getEnabled()) {
				DepositBonusDto depositBonus = conversionService.convert(db, DepositBonusDto.class);
				adjustForCurrency(depositBonus, player.getCurrency());
				responses.add(depositBonus);
			}
		}
		
		return responses;
	}
	
	private void adjustForCurrency(DepositBonusDto depositBonus, String currency) {
		// Eur1=usd1=cad1.5=nzd2=inr100=jpy150
		double multiplier = 1.0; // Default is EUR
		switch (currency.toUpperCase()) {
			case "USD":
				multiplier = 1.0;
			case "CAD":
				multiplier = 1.5;
			case "NZD":
				multiplier = 2.0;
			case "INR":
				multiplier = 100.0;
			case "JPY":
				multiplier = 150.0;
		}
		depositBonus.setMinBonus(currency.toUpperCase() + " " + (new BigDecimal(depositBonus.getMinBonus()).multiply(new BigDecimal(multiplier)).setScale(2)));
		depositBonus.setMaxBonus(currency.toUpperCase() + " " + (new BigDecimal(depositBonus.getMaxBonus()).multiply(new BigDecimal(multiplier)).setScale(2)));
		
		String bonusAmountStr = depositBonus.getBonusAmount();
		if (!bonusAmountStr.contains("%")) {
			BigDecimal bonusAmount = new BigDecimal(bonusAmountStr).setScale(2);
			bonusAmount = bonusAmount.multiply(new BigDecimal(multiplier));
			depositBonus.setBonusAmount(bonusAmount.toString());
		}
	}

	public List<DepositBonus> getAllDepositBonusesByUser(Long userId) {
		// Fetch the user from the given internal ID
		Player player = internalClient.getPlayer(userId).orElseThrow(() -> new PlayerNotFoundException("Could not find player for id: " + userId));
		LOGGER.info("D> Got player: {}", player);
		
		List<DepositBonus> bonusesByUser = combineDepositBonuses(player);

		for (DepositBonus db : bonusesByUser) {
			db.setActiveCountries(bonusCountryRepository.findBonusCountryByBonusId(db.getId()));
			db.setActiveUsers(bonusUserRepository.findBonusUserByBonusId(db.getId()));
		}

		return bonusesByUser;
	}

	private List<DepositBonus> combineDepositBonuses(Player player) {
		// Search for bonuses based on: a) the country of the user, b) whether the user is in the user list
		List<DepositBonus> bonusesByCountry = depositBonusRepository.findByCountryCode(player.getCountryCode());
		List<DepositBonus> defaultBonuses = depositBonusRepository.findByCountryCode(DEFAULT_COUNTRY_CODE);
		List<DepositBonus> bonusesByUser = depositBonusRepository.findByUserId(player.getId());
		
		// Combine the outcomes into a single list of unique deposit bonuses
		bonusesByCountry.addAll(defaultBonuses);
		for (DepositBonus db : bonusesByCountry) {
			if (!bonusesByUser.contains(db)) {
				bonusesByUser.add(db);
			}
		}
		return bonusesByUser;
	}

	public DepositBonus createDepositBonus(DepositBonusRequest depositBonusRequest) {
		LOGGER.info("D> Received bonus req {}", depositBonusRequest);
		DepositBonus depositBonus = conversionService.convert(depositBonusRequest, DepositBonus.class);
		LOGGER.info("D> Converted new DepBonus {}", depositBonus);
		depositBonus = depositBonusRepository.save(depositBonus);

		if (Objects.nonNull(depositBonusRequest.getActiveCountries())) {
			depositBonus.setActiveCountries(updateBonusCountries(depositBonusRequest, depositBonus));
		}
		if (Objects.nonNull(depositBonusRequest.getActiveUsers())) {
			depositBonus.setActiveUsers(updateBonusUsers(depositBonusRequest, depositBonus));
		}
		
		return depositBonus;
	}

	private Set<BonusCountry> updateBonusCountries(BaseDepositBonusRequest depositBonusRequest, DepositBonus depositBonus) {
		// Go get our existing bonuses
		// Go through our new list of countries from the BO
		// If there's a match - no change
		// If an existing bonus has no country - delete it
		// If there's a new country with no match - create it
		Set<String> newCountries = new HashSet<String>(depositBonusRequest.getActiveCountries());
		Set<BonusCountry> existingBonusCountries = bonusCountryRepository.findBonusCountryByBonusId(depositBonus.getId());
		for (BonusCountry bc : existingBonusCountries) {
			boolean match = false;
			for (String country : depositBonusRequest.getActiveCountries()) {
				if (bc.getCountryCode().equalsIgnoreCase(country)) {
					match = true;
					newCountries.remove(bc.getCountryCode());
				}
			}
			if (!match) {
				// Delete this BonusCountry
				bonusCountryRepository.delete(bc);
			}
		}
		// Now add any new countries
		for (String country : newCountries) {
			BonusCountry bonusCountry = BonusCountry.builder()
				.countryCode(country)
				.bonus(depositBonus)
				.build();
			bonusCountry = bonusCountryRepository.save(bonusCountry);
			existingBonusCountries.add(bonusCountry);
		}
		return new HashSet<BonusCountry>(existingBonusCountries);
	}

	private Set<BonusUser> updateBonusUsers(BaseDepositBonusRequest depositBonusRequest, DepositBonus depositBonus) {
		// Go get our existing bonuses
		// Go through our new list of users from the BO
		// If there's a match - no change
		// If an existing bonus has no user - delete it
		// If there's a new user with no match - create it
		Set<Long> newUsers = new HashSet<Long>(depositBonusRequest.getActiveUsers());
		Set<BonusUser> existingBonusUsers = bonusUserRepository.findBonusUserByBonusId(depositBonus.getId());
		for (BonusUser bu : existingBonusUsers) {
			boolean match = false;
			for (Long userId : depositBonusRequest.getActiveUsers()) {
				if (bu.getUserId() == userId) {
					match = true;
					newUsers.remove(bu.getUserId());
				}
			}
			if (!match) {
				// Delete this BonusCountry
				bonusUserRepository.delete(bu);
			}
		}
		// Now add any new countries
		for (Long userId: newUsers) {
			BonusUser bonusUser = BonusUser.builder()
				.userId(userId)
				.bonus(depositBonus)
				.build();
			bonusUser = bonusUserRepository.save(bonusUser);
			existingBonusUsers.add(bonusUser);
		}
		return new HashSet<BonusUser>(existingBonusUsers);
	}
	
	public DepositBonus updateDepositBonus(Long id, UpdateDepositBonusRequest depositBonusRequest) {
		DepositBonus depositBonus = depositBonusRepository.findById(id)
                .orElseThrow(() -> new DepositBonusException("Cannot find deposit bonus with id: " + id));

        if (Objects.nonNull(depositBonusRequest.getActivationDate())) {
        	depositBonus.setActivationDate(depositBonusRequest.getActivationDate());
        }
        if (Objects.nonNull(depositBonusRequest.getEnabled())) {
        	depositBonus.setEnabled(depositBonusRequest.getEnabled());
        }
        if (Objects.nonNull(depositBonusRequest.getExpirationDate())) {
        	depositBonus.setExpirationDate(depositBonusRequest.getExpirationDate());
        }
        if (Objects.nonNull(depositBonusRequest.getInternalDescription())) {
        	depositBonus.setInternalDescription(depositBonusRequest.getInternalDescription());
        }
        if (Objects.nonNull(depositBonusRequest.getPromoImage())) {
        	depositBonus.setPromoImage(depositBonusRequest.getPromoImage());
        }
        if (Objects.nonNull(depositBonusRequest.getTitleKeyword())) {
        	depositBonus.setTitleKeyword(depositBonusRequest.getTitleKeyword());
        }
        if (Objects.nonNull(depositBonusRequest.getDescriptionKeyword())) {
        	depositBonus.setDescriptionKeyword(depositBonusRequest.getDescriptionKeyword());
        }
        if (Objects.nonNull(depositBonusRequest.getTermsKeyword())) {
        	depositBonus.setTermsKeyword(depositBonusRequest.getTermsKeyword());
        }
        if (Objects.nonNull(depositBonusRequest.getMinBonus())) {
        	depositBonus.setMinBonus(depositBonusRequest.getMinBonus());
        }
        if (Objects.nonNull(depositBonusRequest.getMaxBonus())) {
        	depositBonus.setMaxBonus(depositBonusRequest.getMaxBonus());
        }
        
        depositBonus = depositBonusRepository.save(depositBonus);
        if (Objects.nonNull(depositBonusRequest.getActiveCountries())) {
        	depositBonus.setActiveCountries(updateBonusCountries(depositBonusRequest, depositBonus));
        }
        if (Objects.nonNull(depositBonusRequest.getActiveUsers())) {
        	depositBonus.setActiveUsers(updateBonusUsers(depositBonusRequest, depositBonus));
        }
        return depositBonus;
	}

	public void deleteDepositBonus(Long bonusId) {
		depositBonusRepository.deleteById(bonusId);
	}

	public Boolean validateBonusCode(String code, String email) {
		// Fetch the user from the given internal ID
		Player player = internalClient.getPlayer(email).orElseThrow(() -> new PlayerNotFoundException("Could not find player for email: " + email));
		LOGGER.info("D> Got player for code validation: {}", player);

		Optional<DepositBonus> bonus = depositBonusRepository.findByCode(code.toUpperCase());
		if (bonus.isPresent()) {
			DepositBonus foundBonus = bonus.get();
        	foundBonus.setActiveCountries(bonusCountryRepository.findBonusCountryByBonusId(foundBonus.getId()));
        	foundBonus.setActiveUsers(bonusUserRepository.findBonusUserByBonusId(foundBonus.getId()));
			// Everything is in UTC so should match
			Date now = new Date();
			// A valid bonus is if:
			// * it's enabled
			// * it's currently active (after the activation date and before the expiration date)
			// * has a country that's the same as the user (default country counts), OR
			// * has the user's ID against it
			if (foundBonus.getEnabled() == Boolean.TRUE && foundBonus.getActivationDate().before(now) && foundBonus.getExpirationDate().after(now)) {
				if (foundBonus.getActiveCountries() != null) {
					for (BonusCountry bc : foundBonus.getActiveCountries()) {
						if (bc.getCountryCode().equalsIgnoreCase(player.getCountryCode()) || bc.getCountryCode().equalsIgnoreCase(DEFAULT_COUNTRY_CODE)) {
							return Boolean.TRUE;
						}
					}
				}
				if (foundBonus.getActiveUsers() != null) {
					for (BonusUser bu : foundBonus.getActiveUsers()) {
						if (bu.getUserId() == player.getId()) {
							return Boolean.TRUE;
						}
					}
				}
			}
		}
		return Boolean.FALSE;
	}

}
