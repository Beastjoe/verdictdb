package edu.umich.verdict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import edu.umich.verdict.datatypes.SampleParam;
import edu.umich.verdict.datatypes.SampleSizeInfo;
import edu.umich.verdict.datatypes.TableUniqueName;
import edu.umich.verdict.dbms.Dbms;
import edu.umich.verdict.exceptions.VerdictException;
import edu.umich.verdict.util.VerdictLogger;


public abstract class VerdictMeta {
	
	private final String META_SIZE_TABLE;
	
	private final String META_NAME_TABLE;
	
	/**
	 * Works as a cache for a single query execution.
	 * key: sample table
	 * value: sample size info
	 */
	protected Map<TableUniqueName, SampleSizeInfo> sampleSizeMeta;
	
	/**
	 * Works as a cache for a single query execution.
	 * key: original table
	 * value: key: sample creation params
	 * 	      value: sample table
	 */
	protected Map<TableUniqueName, Map<SampleParam, TableUniqueName>> sampleNameMeta;
	
	/**
	 * remembers for what query id and schema, we have updated the meta info.
	 */
	protected Set<Pair<Long, String>> uptodateSchemas;
	
	/**
	 * remembers tables and their column names.
	 */
	protected Map<TableUniqueName, List<String>> tableToColumnNames;
	
	protected VerdictContext vc;

	public VerdictMeta() {
		sampleSizeMeta = new HashMap<TableUniqueName, SampleSizeInfo>();
		sampleNameMeta = new HashMap<TableUniqueName, Map<SampleParam, TableUniqueName>>();
		uptodateSchemas = new HashSet<Pair<Long, String>>();
		tableToColumnNames = new HashMap<TableUniqueName, List<String>>();
		META_NAME_TABLE = vc.getConf().get("verdict.meta_name_table");
		META_SIZE_TABLE = vc.getConf().get("verdict.meta_size_table");
	}
	
	protected Dbms getMetaDbms() {
		return vc.getMetaDbms();
	}
	
	public void clearSampleInfo() {
		sampleSizeMeta.clear();
		sampleNameMeta.clear();
	}
	
	public List<String> getColumnNames(TableUniqueName tableName) {
		refreshSampleInfoIfNeeded(tableName.getSchemaName());
		if (tableToColumnNames.containsKey(tableName)) {
			return tableToColumnNames.get(tableName);
		} else {
			return new ArrayList<String>();
		}
	}
	
	public Map<TableUniqueName, List<String>> getTableAndColumnNames(String schemaName) {
		refreshSampleInfoIfNeeded(schemaName);
		return tableToColumnNames;
	}
	
	/**
	 * Insert sample info into local data structure (for quick access) and into the DBMS (for persistence).
	 * @param originalSchemaName
	 * @param originalTableName
	 * @param sampleSize
	 * @param originalTableSize
	 * @throws VerdictException 
	 */
	public abstract void insertSampleInfo(SampleParam param, long sampleSize, long originalTableSize) throws VerdictException;
		
	/**
	 * Delete sample info from {@link #META_SIZE_TABLE} (for quick access) and from the DBMS (for persistence).
	 * @param originalTableName
	 * @throws VerdictException 
	 */
	public void deleteSampleInfo(SampleParam param) throws VerdictException {
		refreshSampleInfoIfNeeded(param.originalTable.getSchemaName());
		TableUniqueName originalTable = param.originalTable;
		
		if (sampleNameMeta.containsKey(originalTable)) {
			TableUniqueName sampleTableName = sampleNameMeta.get(originalTable).get(param);
			getMetaDbms().deleteSampleNameEntryFromDBMS(param, getMetaNameTableForOriginalTable(originalTable));
			getMetaDbms().deleteSampleSizeEntryFromDBMS(param, getMetaSizeTableForSampleTable(sampleTableName));
		} else {
			VerdictLogger.warn(String.format("No sample table for the parameter: [%s, %s, %.4f, %s]",
					param.originalTable, param.sampleType, param.samplingRatio, param.columnNames.toString()));
		}
	}
	
	public void refreshSampleInfoIfNeeded(String schemaName) {
		if (vc.getConf().getBoolean("refresh_meta_before_every_query")
			&& !uptodateSchemas.contains(Pair.of(vc.getCurrentQid(), schemaName))) {
			refreshSampleInfo(schemaName);
			uptodateSchemas.add(Pair.of(vc.getCurrentQid(), schemaName));
		}
	}
	
	public abstract void refreshSampleInfo(String schemaName);
	
	public Pair<Long, Long> getSampleAndOriginalTableSizeBySampleTableNameIfExists(TableUniqueName sampleTableName) {
		refreshSampleInfoIfNeeded(sampleTableName.getSchemaName());
		if (sampleSizeMeta.containsKey(sampleTableName)) {
			SampleSizeInfo info = sampleSizeMeta.get(sampleTableName);
			return Pair.of(info.sampleSize, info.originalTableSize);
		} else {
			return Pair.of(-1L, -1L);
		}
	}
	
	/**
	 * Returns the sample creation parameters and the names of the created samples for a given original table.
	 * @param originalTableName
	 * @return A list of sample creation parameters and a sample table name.
	 */
	public List<Pair<SampleParam, TableUniqueName>> getSampleInfoFor(TableUniqueName originalTableName) {
		refreshSampleInfoIfNeeded(originalTableName.getSchemaName());
		List<Pair<SampleParam, TableUniqueName>> sampleInfo = new ArrayList<Pair<SampleParam, TableUniqueName>>();
		if (sampleNameMeta.containsKey(originalTableName)) {
			for (Map.Entry<SampleParam, TableUniqueName> e : sampleNameMeta.get(originalTableName).entrySet()) {
				sampleInfo.add(Pair.of(e.getKey(), e.getValue()));
			}
		}
		return sampleInfo;
	}
	
	/**
	 * Returns the sample and original table size for the given sample table name.
	 * @param sampleTableName
	 * @return
	 */
	public SampleSizeInfo getSampleSizeOf(TableUniqueName sampleTableName) {
		return sampleSizeMeta.get(sampleTableName);
	}
	
	public SampleSizeInfo getSampleSizeOf(SampleParam param) {
		TableUniqueName sampleTable = lookForSampleTable(param);
		if (sampleTable == null) {
			return null;
		}
		return vc.getMeta().getSampleSizeOf(sampleTable);
	}
	
	public TableUniqueName lookForSampleTable(SampleParam param) {
		TableUniqueName originalTable = param.originalTable;
		List<Pair<SampleParam, TableUniqueName>> sampleInfo = vc.getMeta().getSampleInfoFor(originalTable);
		TableUniqueName sampleTable = null;
		
		for (Pair<SampleParam, TableUniqueName> e : sampleInfo) {
			SampleParam p = e.getLeft();
			
			if (param.samplingRatio == null) {
				if (p.originalTable.equals(param.originalTable)
						&& p.sampleType.equals(param.sampleType)
						&& p.columnNames.equals(param.columnNames)) {
					sampleTable = e.getRight();
				}
			} else {
				if (p.equals(param)) {
					sampleTable = e.getRight();
				}
			}
		}
		
		if (sampleTable == null) {
			if (param.sampleType.equals("universe") || param.sampleType.equals("stratified")) {
				VerdictLogger.error(this, String.format("No %.2f%% %s sample table on %s found for the table %s.",
						param.samplingRatio*100, param.sampleType, param.columnNames.toString(), originalTable));
			} else if (param.sampleType.equals("uniform")) {
				if (param.samplingRatio != null) {
					VerdictLogger.error(this, String.format("No %.2f%% %s sample table found for the table %s.",
							param.samplingRatio*100, param.sampleType, originalTable));
				} else {
					VerdictLogger.error(this, String.format("No %s sample table found for the table %s.",
							param.sampleType, originalTable));
				}
			}
		}
		
		return sampleTable;
	}
	
	/**
	 * 
	 * @param relatedTableName Either the original table or the sample table.
	 * @return
	 */
	public TableUniqueName getMetaSizeTableForOriginalTable(TableUniqueName originalTable) {
		return getMetaSizeTableForOriginalSchema(originalTable.getSchemaName());
	}
	
	public TableUniqueName getMetaSizeTableForOriginalSchema(String schema) {
		return TableUniqueName.uname(metaCatalogForDataCatalog(schema), META_SIZE_TABLE);
	}
	
	public TableUniqueName getMetaSizeTableForSampleTable(TableUniqueName sampleTable) {
		return getMetaSizeTableForSampleSchema(sampleTable.getSchemaName());
	}
	
	public TableUniqueName getMetaSizeTableForSampleSchema(String schema) {
		return TableUniqueName.uname(schema, META_SIZE_TABLE);
	}
	
	/**
	 * 
	 * @param relatedTableName Either the original table or the sample table.
	 * @return
	 */
	public TableUniqueName getMetaNameTableForOriginalTable(TableUniqueName originalTable) {
		return TableUniqueName.uname(metaCatalogForDataCatalog(originalTable.getSchemaName()), META_NAME_TABLE);
	}
	
	public TableUniqueName getMetaNameTableForOriginalSchema(String schema) {
		return TableUniqueName.uname(metaCatalogForDataCatalog(schema), META_NAME_TABLE);
	}
	
	public TableUniqueName getMetaNameTableForSampleTable(TableUniqueName sampleTable) {
		return TableUniqueName.uname(sampleTable.getSchemaName(), META_NAME_TABLE);
	}
	
	public TableUniqueName getMetaNameTableForSampleSchema(String schema) {
		return TableUniqueName.uname(schema, META_NAME_TABLE);
	}
	
	public String metaCatalogForDataCatalog(String dataCatalog) {
		return dataCatalog + vc.getConf().get("verdict.meta_catalog_suffix");
	}
	
}
