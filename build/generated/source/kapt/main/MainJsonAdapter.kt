// Code generated by moshi-kotlin-codegen. Do not edit.
@file:Suppress("DEPRECATION", "unused", "ClassName", "REDUNDANT_PROJECTION",
    "RedundantExplicitType", "LocalVariableName", "RedundantVisibilityModifier",
    "PLATFORM_CLASS_MAPPED_TO_KOTLIN")

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.`internal`.Util
import java.lang.NullPointerException
import kotlin.Double
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.emptySet
import kotlin.text.buildString

public class MainJsonAdapter(
  moshi: Moshi
) : JsonAdapter<Main>() {
  private val options: JsonReader.Options = JsonReader.Options.of("temp")

  private val doubleAdapter: JsonAdapter<Double> = moshi.adapter(Double::class.java, emptySet(),
      "temp")

  public override fun toString(): String = buildString(26) {
      append("GeneratedJsonAdapter(").append("Main").append(')') }

  public override fun fromJson(reader: JsonReader): Main {
    var temp: Double? = null
    reader.beginObject()
    while (reader.hasNext()) {
      when (reader.selectName(options)) {
        0 -> temp = doubleAdapter.fromJson(reader) ?: throw Util.unexpectedNull("temp", "temp",
            reader)
        -1 -> {
          // Unknown name, skip it.
          reader.skipName()
          reader.skipValue()
        }
      }
    }
    reader.endObject()
    return Main(
        temp = temp ?: throw Util.missingProperty("temp", "temp", reader)
    )
  }

  public override fun toJson(writer: JsonWriter, value_: Main?): Unit {
    if (value_ == null) {
      throw NullPointerException("value_ was null! Wrap in .nullSafe() to write nullable values.")
    }
    writer.beginObject()
    writer.name("temp")
    doubleAdapter.toJson(writer, value_.temp)
    writer.endObject()
  }
}
