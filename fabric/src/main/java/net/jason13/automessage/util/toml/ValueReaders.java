package net.jason13.automessage.util.toml;

import java.util.concurrent.atomic.AtomicInteger;

import static net.jason13.automessage.util.toml.ArrayValueReader.ARRAY_VALUE_READER;
import static net.jason13.automessage.util.toml.BooleanValueReaderWriter.BOOLEAN_VALUE_READER_WRITER;
import static net.jason13.automessage.util.toml.DateValueReaderWriter.DATE_VALUE_READER_WRITER;
import static net.jason13.automessage.util.toml.InlineTableValueReader.INLINE_TABLE_VALUE_READER;
import static net.jason13.automessage.util.toml.LiteralStringValueReader.LITERAL_STRING_VALUE_READER;
import static net.jason13.automessage.util.toml.MultilineLiteralStringValueReader.MULTILINE_LITERAL_STRING_VALUE_READER;
import static net.jason13.automessage.util.toml.MultilineStringValueReader.MULTILINE_STRING_VALUE_READER;
import static net.jason13.automessage.util.toml.NumberValueReaderWriter.NUMBER_VALUE_READER_WRITER;
import static net.jason13.automessage.util.toml.StringValueReaderWriter.STRING_VALUE_READER_WRITER;

class ValueReaders {
  
  static final ValueReaders VALUE_READERS = new ValueReaders();
  
  Object convert(String value, AtomicInteger index, Context context) {
    String substring = value.substring(index.get());
    for (ValueReader valueParser : READERS) {
      if (valueParser.canRead(substring)) {
        return valueParser.read(value, index, context);
      }
    }
    
    Results.Errors errors = new Results.Errors();
    errors.invalidValue(context.identifier.getName(), substring, context.line.get());
    return errors;
  }
  
  private ValueReaders() {}
  
  private static final ValueReader[] READERS = { 
    MULTILINE_STRING_VALUE_READER, MULTILINE_LITERAL_STRING_VALUE_READER, LITERAL_STRING_VALUE_READER, STRING_VALUE_READER_WRITER, DATE_VALUE_READER_WRITER, NUMBER_VALUE_READER_WRITER, BOOLEAN_VALUE_READER_WRITER, ARRAY_VALUE_READER, INLINE_TABLE_VALUE_READER
  };
}
