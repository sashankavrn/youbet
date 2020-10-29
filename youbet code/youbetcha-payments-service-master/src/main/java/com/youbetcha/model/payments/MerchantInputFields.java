package com.youbetcha.model.payments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantInputFields {
    private String key;
    private String name;
    private String description;
    private String defaultValue;
    private ArrayList<LookUpValues> lookUpValues;
}
