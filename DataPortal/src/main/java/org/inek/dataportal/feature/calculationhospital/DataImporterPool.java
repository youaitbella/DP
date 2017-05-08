package org.inek.dataportal.feature.calculationhospital;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kunkelan
 */
public class DataImporterPool implements Serializable {

    private final Map<String, DataImporter<?>> importers = new HashMap<>();

    public DataImporterPool() {
        importers.put("peppradiology", DataImporter.obtainDataImporter("peppradiology"));
        importers.put("pepplaboratory", DataImporter.obtainDataImporter("pepplaboratory"));
        importers.put("pepptherapy", DataImporter.obtainDataImporter("pepptherapy"));
        importers.put("peppmedinfra", DataImporter.obtainDataImporter("peppmedinfra"));
        importers.put("peppnonmedinfra", DataImporter.obtainDataImporter("peppnonmedinfra"));
        importers.put("peppcostcenter", DataImporter.obtainDataImporter("peppcostcenter"));
        importers.put("peppstationservicecost", DataImporter.obtainDataImporter("peppstationservicecost"));
    }

    public DataImporter<?> getDataImporter(String importerName) {
        if (!importers.containsKey(importerName)) {
            throw new IllegalArgumentException("unknown importer " + importerName);
        }
        return importers.get(importerName);
    }

}
