package pl.ciruk.experiments._1_optional;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 10, time = 5)
@Measurement(iterations = 10, time = 5)
@Fork(1)
@State(Scope.Thread)
public class OptionalBenchmark {
    private Map<String, String> itemsByName;

    @Setup
    public void initialize() {
        itemsByName = Map.of("First", UUID.randomUUID().toString(), "Second", UUID.randomUUID().toString());
    }

    @Benchmark
    public void emptyOptionalMap(Blackhole bh) {
        var optional = Optional.ofNullable(itemsByName.get("Third"))
                .map(String::toLowerCase);

        bh.consume(optional);
    }

    @Benchmark
    public void nullMap(Blackhole bh) {
        String third = itemsByName.get("Third");
        if (third != null) {
            third = third.toLowerCase();
        }

        bh.consume(third);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(OptionalBenchmark.class.getSimpleName())
                .shouldDoGC(true)
                .jvmArgs("-verbose:gc")
                .build();

        new Runner(opt).run();
    }
}
