package ext.ucoe.plmmigrationassistant.service;
import ext.ucoe.plmmigrationassistant.domain.*;
import ext.ucoe.plmmigrationassistant.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
@Service
public class CsvImportService {
  private final MigrationProjectRepository projects;
  private final SourceImportRepository imports;
  private final SourceTableRepository tables;
  private final SourceColumnProfileRepository cols;
  public CsvImportService(MigrationProjectRepository p,
                          SourceImportRepository i, SourceTableRepository t,
                          SourceColumnProfileRepository c) {
    projects = p;
    imports = i;
    tables = t;
    cols = c;
  }
  @Transactional
  public SourceImport importCsv(Long projectId, MultipartFile file)
      throws IOException {
    MigrationProject p = projects.findById(projectId).orElseThrow();
    List<String> lines =
        new BufferedReader(new InputStreamReader(file.getInputStream(),
                                                 StandardCharsets.UTF_8))
            .lines()
            .toList();
    if (lines.isEmpty())
      throw new IllegalArgumentException("CSV file is empty");
    String[] headers = split(lines.get(0));
    List<String[]> rows = lines.stream()
                              .skip(1)
                              .filter(s -> !s.isBlank())
                              .map(this::split)
                              .toList();
    SourceImport si = new SourceImport();
    si.project = p;
    si.importName = file.getOriginalFilename();
    si.fileName = file.getOriginalFilename();
    si.sourceSystem = p.sourceSystem;
    si.importType = "CSV";
    si.status = "IMPORTED";
    imports.save(si);
    SourceTable st = new SourceTable();
    st.sourceImport = si;
    st.tableName = tableName(file.getOriginalFilename());
    st.displayName = st.tableName;
    st.rowCount = rows.size();
    st.columnCount = headers.length;
    tables.save(st);
    for (int i = 0; i < headers.length; i++) {
      Set<String> distinct = new LinkedHashSet<>();
      List<String> samples = new ArrayList<>();
      long nulls = 0;
      int max = 0;
      for (String[] row : rows) {
        String v = i < row.length ? row[i].trim() : "";
        if (v.isEmpty())
          nulls++;
        else {
          distinct.add(v);
          if (samples.size() < 5)
            samples.add(v);
          max = Math.max(max, v.length());
        }
      }
      SourceColumnProfile c = new SourceColumnProfile();
      c.sourceTable = st;
      c.columnName = headers[i].trim();
      c.dataTypeGuess = "string";
      c.nullCount = nulls;
      c.nullPercentage = rows.isEmpty() ? 0 : (100.0 * nulls / rows.size());
      c.distinctCount = distinct.size();
      c.maxLength = max;
      c.duplicateCount = Math.max(0, rows.size() - distinct.size());
      c.sampleValues = String.join(" | ", samples);
      c.likelyEnum = distinct.size() > 0 && distinct.size() <= 10;
      c.duplicateIndicator = c.duplicateCount > 0;
      cols.save(c);
    }
    return si;
  }
  private String[] split(String line) { return line.split(",", -1); }
  private String tableName(String filename) {
    String n =
        filename == null ? "CSV_TABLE" : filename.replaceFirst("\\.[^.]+$", "");
    return n.toUpperCase().replaceAll("[^A-Z0-9]+", "_");
  }
}
