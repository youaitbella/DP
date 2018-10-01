package org.inek.dataportal.calc.backingbean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kunkelan
 */
public class DataImporterPool implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Map<String, DataImporter<?, ?>> importers = new HashMap<>();

    @SuppressWarnings("MultipleStringLiterals")
    public DataImporterPool() {
        importers.put("peppradiology", DataImporter.obtainDataImporter("peppradiology"));
        importers.put("pepplaboratory", DataImporter.obtainDataImporter("pepplaboratory"));
        importers.put("pepptherapy", DataImporter.obtainDataImporter("pepptherapy"));
        importers.put("peppmedinfra", DataImporter.obtainDataImporter("peppmedinfra"));
        importers.put("peppnonmedinfra", DataImporter.obtainDataImporter("peppnonmedinfra"));
        importers.put("peppcostcenter", DataImporter.obtainDataImporter("peppcostcenter"));
        importers.put("peppcostcenter11", DataImporter.obtainDataImporter("peppcostcenter11"));
        importers.put("peppcostcenter12", DataImporter.obtainDataImporter("peppcostcenter12"));
        importers.put("peppcostcenter13", DataImporter.obtainDataImporter("peppcostcenter13"));
        importers.put("drgcostcenter", DataImporter.obtainDataImporter("drgcostcenter"));
        importers.put("drgcostcenter11", DataImporter.obtainDataImporter("drgcostcenter11"));
        importers.put("drgcostcenter12", DataImporter.obtainDataImporter("drgcostcenter12"));
        importers.put("drgcostcenter13", DataImporter.obtainDataImporter("drgcostcenter13"));
        importers.put("peppstationservicecost", DataImporter.obtainDataImporter("peppstationservicecost"));
        importers.put("drgnormalward", DataImporter.obtainDataImporter("drgnormalward"));
        importers.put("drgintensive", DataImporter.obtainDataImporter("drgintensive"));
        importers.put("drgstrokeunit", DataImporter.obtainDataImporter("drgstrokeunit"));
        importers.put("drgmedinfra", DataImporter.obtainDataImporter("drgmedinfra"));
        importers.put("drgnonmedinfra", DataImporter.obtainDataImporter("drgnonmedinfra"));
        importers.put("drglaboratory", DataImporter.obtainDataImporter("drglaboratory"));
        importers.put("drgradiology", DataImporter.obtainDataImporter("drgradiology"));
    }

    public DataImporter<?,?> getDataImporter(String importerName) {
        if (!importers.containsKey(importerName.toLowerCase())) {
            throw new IllegalArgumentException("unknown importer " + importerName);
        }
        return importers.get(importerName);
    }

}
