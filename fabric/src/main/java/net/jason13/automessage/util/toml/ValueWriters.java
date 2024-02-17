package net.jason13.automessage.util.toml;

import static net.jason13.automessage.util.toml.BooleanValueReaderWriter.BOOLEAN_VALUE_READER_WRITER;
import static net.jason13.automessage.util.toml.DateValueReaderWriter.DATE_PARSER_JDK_6;
import static net.jason13.automessage.util.toml.DateValueReaderWriter.DATE_VALUE_READER_WRITER;
import static net.jason13.automessage.util.toml.MapValueWriter.MAP_VALUE_WRITER;
import static net.jason13.automessage.util.toml.NumberValueReaderWriter.NUMBER_VALUE_READER_WRITER;
import static net.jason13.automessage.util.toml.ObjectValueWriter.OBJECT_VALUE_WRITER;
import static net.jason13.automessage.util.toml.PrimitiveArrayValueWriter.PRIMITIVE_ARRAY_VALUE_WRITER;
import static net.jason13.automessage.util.toml.StringValueReaderWriter.STRING_VALUE_READER_WRITER;
import static net.jason13.automessage.util.toml.TableArrayValueWriter.TABLE_ARRAY_VALUE_WRITER;

class ValueWriters {

  static final ValueWriters WRITERS = new ValueWriters();

  ValueWriter findWriterFor(Object value) {
    for (ValueWriter valueWriter : VALUE_WRITERS) {
      if (valueWriter.canWrite(value)) {
        return valueWriter;
      }
    }

    return OBJECT_VALUE_WRITER;
  }

  private ValueWriters() {}
  
  private static DateValueReaderWriter getPlatformSpecificDateConverter() {
    String specificationVersion = Runtime.class.getPackage().getSpecificationVersion();
    return specificationVersion != null && specificationVersion.startsWith("1.6") ? DATE_PARSER_JDK_6 : DATE_VALUE_READER_WRITER;
  }

  private static final ValueWriter[] VALUE_WRITERS = {
      STRING_VALUE_READER_WRITER, NUMBER_VALUE_READER_WRITER, BOOLEAN_VALUE_READER_WRITER, getPlatformSpecificDateConverter(),
      MAP_VALUE_WRITER, PRIMITIVE_ARRAY_VALUE_WRITER, TABLE_ARRAY_VALUE_WRITER
  };
}
