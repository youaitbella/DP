package org.inek.dataportal.feature.calculationhospital;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kunkelan
 */
public class DataImporterPool implements Serializable {

    private Map<String, DataImporter<?>> importers = new HashMap<>();

    public DataImporterPool() {
        importers.put("peppmedinfra", DataImporter.obtainDataImporter("peppmedinfra"));
    }

    public DataImporter<?> getDataImporter(String importerName) {
        if (!importers.containsKey(importerName)) {
            throw new IllegalArgumentException("unknown importer " + importerName);
        }
        return importers.get(importerName);
    }

}
