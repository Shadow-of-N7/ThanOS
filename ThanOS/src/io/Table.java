package io;

import collections.ObjectList;
import collections.StringBuilder;

public class Table {
    // Use an object list to reduce code duplication
    private final ObjectList _rows;
    private byte _defaultColor = 0x07;

    public Table() {
        _rows = new ObjectList();
    }

    @SJC.Inline
    public byte getDefaultColor() {
        return _defaultColor;
    }

    @SJC.Inline
    public void setDefaultColor(byte color) {
        _defaultColor = color;
    }

    /**
     *
     * @return The index of the new
     */
    public int addRow() {
        _rows.add(new Row());
        return _rows.getLength() - 1;
    }

    public void setRow(int index, Row row) {
        _rows.setElementAt(index, row);
    }

    public void removeRow(int index) {
        _rows.removeAt(index);
    }

    public Row getRow(int index) {
        return (Row) _rows.elementAt(index);
    }

    public void setCell(int column, int row, String text) {
        ((Row) _rows.elementAt(column)).setCell(row, text);
    }

    public void print() {
        String table = toString(true);
        Console.print(table);
        close();
    }

    private void close() {
        Console.resetColor();
        Console.println();
    }

    /**
     * Returns the maximum index of all columns within the table.
     * @return
     */
    public int getMaxRowIndex() {
        return _rows.getLength() - 1;
    }

    /**
     * Returns the maximum index of all rows within the table.
     * @return
     */
    public int getMaxCellIndex() {
        int maxIndex = 0;
        for(int i = 0; i < getMaxRowIndex() + 1; i++) {
            int currentLength = getRow(i).getMaxCellIndex();
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
        int columns = getMaxRowIndex() + 1;
        int rows = getMaxCellIndex() + 1;
        // Get maximum row length of each column
        for(int x = 0; x < rows; x++) {
            int maxRowLength = 0;
            for(int y = 0; y < columns; y++) {
                // Get the maximum length of each row within the current column
                // Skip if cell empty
                if(getRow(y).getCell(x) == null) {
                    break;
                }
                // Update else
                if(getRow(y).getCell(x).text.length() > maxRowLength) {
                    maxRowLength = getRow(y).getCell(x).text.length();
                }
            }

            // Fill all row segments with spaces to align the rows
            for(int y = 0; y < columns; y++) {
                Row row = getRow(y);
                String segment = row.getCell(x).text;

                // Fill missing cells to prevent null pointers
                while (row.getMaxCellIndex() < getMaxCellIndex()) {
                    row.addCell("");
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
                    // Backup old color
                    byte color = row.getCell(x).color;
                    // Place new string
                    setCell(y, x, new String(newSegment));
                    // Patch new cell with the saved color
                    row.getCell(x).setColor(color);
                }
            }
        }
        // Create one single string
        StringBuilder builder = new StringBuilder();
        for(int y = 0; y < columns; y++) {
            builder.add(getColorBytes(_defaultColor));
            for(int x = 0; x < rows; x++) {
                byte color = getRow(y).getCell(x).color;
                // Set cell color if necessary
                if(color != (byte)0x0) {
                    builder.add(getColorBytes(color));
                }
                builder.add(getRow(y).getCell(x).text);
                // Reset cell color
                if(color != _defaultColor) {
                    builder.add(getColorBytes(_defaultColor));
                }

                // Vertical spacers
                if(columnSpacers) {
                    builder.add(' ');
                    builder.add((char)179);
                    builder.add(' ');
                }
                else {
                    builder.add(' ');
                }

            }
            if(y < columns - 1) {
                builder.add(getColorBytes((byte)0));
                builder.add('\n');
            }
        }
        return builder.toString();
    }

    /**
     * Returns a 2 byte String, containing the color indicator prefix and the color byte.
     * @param color The color to include into the string.
     * @return
     */
    private String getColorBytes(byte color) {
        char[] chars = new char[2];
        chars[0] = 0x07;
        chars[1] = (char) color;
        return new String(chars);
    }

    public static class Row {
        private final ObjectList _row;

        public Row() {
            _row = new ObjectList();
        }

        public Cell addCell(String text) {
            Cell cell = new Cell(text);
            _row.add(cell);
            return cell;
        }

        public int getMaxCellIndex() {
            return _row.getLength() - 1;
        }

        public Cell getCell(int index) {
            return (Cell)_row.elementAt(index);
        }

        public Cell setCell(int index, String text) {
            Cell cell = new Cell(text);
            _row.setElementAt(index, cell);
            return cell;
        }
    }


    public static class Cell {
        public String text;
        public byte color;

        public Cell(String text) {
            this.text = text;
            color = (byte) 0x0;
        }

        public Cell(String text, byte color) {
            this.text = text;
            this.color = color;
        }

        public void setColor(byte foregroundColor, byte backgroundColor, boolean bright, boolean blink) {
            color = Console.ConsoleColor.createColor(foregroundColor, backgroundColor, bright, blink);
        }

        public void setColor(byte color) {
            this.color = color;
        }
    }
}
