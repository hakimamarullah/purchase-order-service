package com.starline.purchase.order.config;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/15/2024 10:24 PM
@Last Modified 10/15/2024 10:24 PM
Version 1.0
*/

import org.hibernate.boot.Metadata;
import org.hibernate.boot.model.relational.ColumnOrderingStrategyLegacy;
import org.hibernate.cfg.MappingSettings;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Table;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Configuration
public class AlphabeticalColumnOrderingStrategy extends ColumnOrderingStrategyLegacy implements HibernatePropertiesCustomizer {

    @Override
    public List<Column> orderTableColumns(Table table, Metadata metadata) {
        return table.getColumns().stream()
                .sorted(Comparator.comparing(Column::getName))
                .toList();
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(MappingSettings.COLUMN_ORDERING_STRATEGY, this);
    }

}
