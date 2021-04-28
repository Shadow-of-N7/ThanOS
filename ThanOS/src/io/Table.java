package io;

import collections.ObjectList;
import collections.StringBuilder;

public class Table {
    // Use an object list to reduce code duplication
    private final ObjectList _columns;

    public Table() {
        _columns = new ObjectList();
    }

    public void addColumn() {
        _columns.add(new Column());
    }

    public void setColumn(int index, Column column) {
        _columns.setElementAt(index, column);
    }

    public void removeColumn(int index) {
        _columns.removeAt(index);
    }

    public Column getColumn(int index) {
        return (Column) _columns.elementAt(index);
    }

    public void setCell(int column, int row, String text) {
        ((Column)_columns.elementAt(column)).setCell(row, text);
    }

    /**
     * Returns the maximum index of all columns within the table.
     * @return
     */
    public int getMaxColumnIndex() {
        return _columns.getLength() - 1;
    }

    /**
     * Returns the maximum index of all rows within the table.
     * @return
     */
    public int getMaxCellIndex() {
        int maxIndex = 0;
        for(int i = 0; i < getMaxColumnIndex() + 1; i++) {
            int currentLength = getColumn(i).getMaxCellIndex();
            if(currentLength > maxIndex) {
                maxIndex = currentLength;
            }
        }
        return maxIndex;
    }

    /**
     * Returns the printable string representation of the table.
     * @param columnSpacers Whether to generate spacers between the columns.
     * @return
     */
    public String toString(boolean columnSpacers) {
        int counter = 0;
        // Get maximum row length of each column
        for(int x = 0; x < getMaxCellIndex() + 1; x++) {
            int maxRowLength = 0;
            for(int y = 0; y < getMaxColumnIndex() + 1; y++) {
                // Get the maximum length of each row within the current column
                // Skip if cell empty
                if(getColumn(y).getCell(x) == null) {
                    break;
                }
                // Update else
                if(getColumn(y).getCell(x).length() > maxRowLength) {
                    maxRowLength = getColumn(y).getCell(x).length();
                }
            }

            // Fill all row segments with spaces to align the rows
            for(int y = 0; y < getMaxColumnIndex() + 1; y++) {
                Column column = getColumn(y);
                String segment = column.getCell(x);

                // Fill missing cells to prevent null pointers
                while (column.getMaxCellIndex() < getMaxCellIndex()) {
                    column.addCell("");
                }

                // Fill missing spaces if defined
                if(segment.length() < maxRowLength) {
                    char[] newSegment = new char[maxRowLength];
                    // Fill in existing text
                    for(int i = 0; i< segment.length(); i++) {
                        newSegment[i] = segment.charAt(i);
                    }
                    // Fill up remaining space with spaces
                    for(int i = 0; i < newSegment.length - segment.length(); i++) {
                        newSegment[segment.length() + i] = ' ';
                    }
                    // Place new string
                    setCell(y, x, new String(newSegment));
                }
            }
        }
        // Create one single string
        StringBuilder builder = new StringBuilder();
        for(int y = 0; y < getMaxColumnIndex() + 1; y++) {
            for(int x = 0; x < getMaxCellIndex() + 1; x++) {
                builder.add(getColumn(y).getCell(x));
                if(columnSpacers) {
                    builder.add('|');
                }
                else {
                    builder.add(' ');
                }
            }
            builder.add('\n');
        }
        return builder.toString();
    }

    public static class Column {
        private final ObjectList _row;

        public Column() {
            _row = new ObjectList();
        }

        public void addCell(String text) {
            _row.add(text);
        }

        public int getMaxCellIndex() {
            return _row.getLength() - 1;
        }

        public String getCell(int index) {
            return (String)_row.elementAt(index);
        }

        public void setCell(int index, String text) {
            _row.setElementAt(index, text);
        }
    }

}
