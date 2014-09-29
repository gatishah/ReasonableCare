package reasonablecare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Utility class to print data in a neat manner
 *
 */
public class Table {

  static final char[] blanks, dashes;

  static {
    Arrays.fill((blanks = new char[100]), ' ');
    Arrays.fill((dashes = new char[100]), '-');
  }

  private final List<String> columnNames = new ArrayList<String>();
  private final int[] columnLengths;

  private final List<List<String>> data = new ArrayList<List<String>>();

  //constructor for the table
  public Table(String col1, String... cols) {
    columnNames.add(col1);
    Collections.addAll(columnNames, cols);
    columnLengths = new int[columnNames.size()];

    // Get the length of each header
    int column = 0;
    for (String header : columnNames) {
      columnLengths[column] = Math.max(columnLengths[column], header.length());
      ++column;
    }
  }

  /**
   * Add a row to the table
   * @param data
   */
  public void add(Object... data) {
    if (data.length != columnNames.size()) {
      throw new RuntimeException("Incorrect number of columns");
    }

    List<String> row = new ArrayList<String>();
    int column = 0;
    for (Object datum : data) {
      String str = toString(datum);
      row.add(str);
      columnLengths[column] = Math.max(columnLengths[column], str.length());

      ++column;
    }
    this.data.add(row);
  }

  private static String toString(Object obj) {
    return (obj == null) ? "null" : obj.toString();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    // Header row = +-------+----+
    appendSeparatorRow(sb, dashes);

    // Headers   = | Header1 | Header 2 |
    appendList(sb, columnNames);

    // Header/data separator = +------+----+
    appendSeparatorRow(sb, dashes);

    // Print data
    for (List<String> row : this.data) {
      appendList(sb, row);
    }

    // Final row = +-----+---+
    appendSeparatorRow(sb, dashes);

    return sb.toString();
  }

  private void appendList(StringBuilder sb, List<String> data) {
    for (int column = 0; column < data.size(); ++column) {
      String datum = data.get(column);
      sb.append("| ").append(datum);
      sb.append(blanks, 0, columnLengths[column] - datum.length());
      sb.append(' '); // Extra blank on right side
    }
    sb.append("|\n");
  }

  private void appendSeparatorRow(StringBuilder sb, char[] dashes) {
    sb.append("+");
    for (int column = 0; column < columnNames.size(); ++column) {
      sb.append(dashes, 0, columnLengths[column] + 2); // Space on either side
      sb.append("+");
    }
    sb.append('\n');
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((columnNames == null) ? 0 : columnNames.hashCode());
    result = prime * result + ((data == null) ? 0 : data.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof Table))
      return false;
    Table other = (Table) obj;
    if (columnNames == null) {
      if (other.columnNames != null)
        return false;
    } else if (!columnNames.equals(other.columnNames))
      return false;
    if (data == null) {
      if (other.data != null)
        return false;
    } else if (!data.equals(other.data))
      return false;
    return true;
  }

}
