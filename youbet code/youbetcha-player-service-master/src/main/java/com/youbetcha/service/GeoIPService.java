package com.youbetcha.service;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.net.InetAddress;

@Service
public class GeoIPService {
    // Reusing thread-safe databaseReader across lookups improves performance
    private DatabaseReader databaseReader;
    private static final Logger LOGGER = LoggerFactory.getLogger(GeoIPService.class);

    GeoIPService() {
        LOGGER.info("GeoIPService()");

        //Create and retain singleton GeoIP2-Country.mmdb DatabaseReader, as expensive to recreate.
        try {
            databaseReader = new DatabaseReader.Builder(ResourceUtils.getFile("classpath:GeoIP2-Country.mmdb")).withCache(new CHMCache()).build();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Cannot access GeoIP2-Country.mmdb (cached). Country codes will default to XX", e);
            try {
                //If cannot create cached DatabaseReader, try non-cached
                databaseReader = new DatabaseReader.Builder(ResourceUtils.getFile("classpath:GeoIP2-Country.mmdb")).build();
            } catch (IOException e1) {
                e1.printStackTrace();
                LOGGER.error("Cannot access GeoIP2-Country.mmdb (non-cached). Country codes will default to XX", e);
                databaseReader = null;
            }
        }

    }

    public String getCountryISO2ByIP(String ip) {
        try {
            return databaseReader.country(InetAddress.getByName(ip)).getCountry().getIsoCode();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("IOException trying to obtain ISO2 country code for " + ip + " will default to XX", e);
        } catch (GeoIp2Exception e) {
            e.printStackTrace();
            LOGGER.error("GeoIp2Exception trying to obtain ISO2 country code for " + ip + " will default to XX", e);
        }
        return "XX";
    }
}
