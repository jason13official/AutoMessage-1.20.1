package net.jason13.automessage.util.toml;

import java.util.Collection;

import static net.jason13.automessage.util.toml.ValueWriters.WRITERS;

class TableArrayValueWriter extends ArrayValueWriter {
  static final ValueWriter TABLE_ARRAY_VALUE_WRITER = new TableArrayValueWriter();

  @Override
  public boolean canWrite(Object value) {
    return isArrayish(value) && !isArrayOfPrimitive(value);
  }

  @Override
  public void write(Object from, WriterContext context) {
    Collection<?> values = normalize(from);

    WriterContext subContext = context.pushTableFromArray();

    for (Object value : values) {
      WRITERS.findWriterFor(value).write(value, subContext);
    }
  }

  private TableArrayValueWriter() {}

  @Override
  public String toString() {
    return "table-array";
  }
}
