package qxo.benchmark.rhino;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@Fork(2)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class RhinoJavaMembersBenchmark {


	private static Map<String,Script> cachedScript =  new ConcurrentHashMap<>();
	static {
			System.setProperty("rhino_JavaMembers_lazyInit", "true");
		   //System.setProperty("rhino_JavaMembers_reflect_cache_on", "false");
			//System.setProperty("rhino_creator_4_JavaMembers", "old");
			
	}
	
	private static final String[] TEST_SAMPLES = new String[]{
			"vo.getName1()"
	};
	
	@Benchmark
	public Object base4Member010p(Blackhole bh) {
		return runJsOnVo(bh, new Member10pBean() , null);
	}
	
	private static final class Holder4JavaMembersOld {
		public static final Holder4JavaMembersOld INST = new Holder4JavaMembersOld();
		static {
			//System.setProperty("rhino_JavaMembers_lazyInit", "true");
			//System.setProperty("rhino_JavaMembers_reflect_cache_on", "false");
			System.setProperty("rhino_creator_4_JavaMembers", "old");
		}
	}
	
	
	
	
	private static final class Holder4JavaMembersLazyInitOff {
		public static final Holder4JavaMembersLazyInitOff INST = new Holder4JavaMembersLazyInitOff();
		static {
			System.setProperty("rhino_JavaMembers_lazyInit", "false");
			//System.setProperty("rhino_JavaMembers_reflect_cache_on", "false");
			//System.setProperty("rhino_creator_4_JavaMembers", "old");
		}
	}
	
	private static final class Holder4JavaMembersLazyInitOn{
		public static final Holder4JavaMembersLazyInitOff INST = new Holder4JavaMembersLazyInitOff();
		static {
			System.setProperty("rhino_JavaMembers_lazyInit", "true");
			//System.setProperty("rhino_JavaMembers_reflect_cache_on", "false");
			//System.setProperty("rhino_creator_4_JavaMembers", "old");
		}
	}
	
	private static final class Holder4JavaMembersReflectCacheOff {
		public static final Holder4JavaMembersReflectCacheOff INST = new Holder4JavaMembersReflectCacheOff();
		static {
			//System.setProperty("rhino_JavaMembers_lazyInit", "true");
			System.setProperty("rhino_JavaMembers_reflect_cache_on", "false");
			//System.setProperty("rhino_creator_4_JavaMembers", "old");
		}
	}
	
	@Benchmark
	public Object old4Member050p(Blackhole bh) {
		return runJsOnVo(bh, new Member50pBean() , Holder4JavaMembersOld.INST);
	}
	@Benchmark
	public Object lazyInitOff4Member050p(Blackhole bh) {
		return runJsOnVo(bh, new Member50pBean() , Holder4JavaMembersLazyInitOff.INST);
	}
	
	@Benchmark
	public Object lazyInitOn4Member050p(Blackhole bh) {
		return runJsOnVo(bh, new Member50pBean() , Holder4JavaMembersLazyInitOn.INST);
	}
	
	@Benchmark
	public Object reflectCacheOff4Member050p(Blackhole bh) {
		return runJsOnVo(bh, new Member50pBean() , Holder4JavaMembersReflectCacheOff.INST);
	}
	
	
	@Benchmark
	public Object base4Member001p(Blackhole bh) {
		return runJsOnVo(bh, new Member1pBean() , null);
	}
	
	
	@Benchmark
	public Object base4Member020p(Blackhole bh) {
		return runJsOnVo(bh, new Member20pBean() , null);
	}
	
	@Benchmark
	public Object base4Member050p(Blackhole bh) {
		return runJsOnVo(bh, new Member50pBean() , null);
	}
	
	
	

	
	
	@Benchmark
	public Object base4Member100p(Blackhole bh) {
		return runJsOnVo(bh, new Member100pBean() , null);
	}
	
	@Benchmark
	public Object base4Member200p(Blackhole bh) {
		return runJsOnVo(bh, new Member200pBean() , null);
	}
	protected  static Object runJsOnVo(Blackhole bh,  AbstractBean vo ,Object initor) {
		vo.setName1("abc");
		Object ret = null;
		for(final String str : TEST_SAMPLES){
			ret =  runJsScript(str,vo ,true);
			if(bh != null) {
				bh.consume(ret);
			}
		}
		return ret;
	}
	
	public  static Object runJsScript(String jsbody,Object vo,boolean initStandardObjects) {
				Object ret =null;
			 Context cx = Context.enter();
		      try {
		    	  Scriptable  scope = initStandardObjects ? cx.initStandardObjects():
	    	  			ScriptRuntime.initSafeStandardObjects(cx, null, false);
		    	  scope.put("vo", scope, vo);
			      Script script = getScript(cx,jsbody);
		          ret = script.exec(cx, scope);
		      } finally {
		          Context.exit();
		      }
			return ret;
		}
	
	private static Script getScript( Context cx, String js) {
		Script script = cachedScript.get(js);
		if(script == null) {
			script = cx.compileString(js, "test"+js.hashCode(), 1, null);
			cachedScript.put(js, script);
		}
		return script;
	}
	
	  public static void main(String[] args) throws RunnerException {
		    Options opt =
		        new OptionsBuilder()
		            .include(RhinoJavaMembersBenchmark.class.getSimpleName())
//		            .mode(Mode.Throughput)
//		            .warmupIterations(8)
//		            .measurementIterations(8)
//		            .forks(2)
//		            .measurementTime(TimeValue.seconds(1))
//		            .warmupTime(TimeValue.seconds(1))
//		            .threads(2)
//		            .timeUnit(TimeUnit.MILLISECONDS)
		            .build();
		    new Runner(opt).run();
		  }
/*
 REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

ManyMember=100, SmallMember=1   Small vs Many > 3x faster
==============================================
Benchmark                                    Mode  Cnt   Score   Error   Units  Score/min
RhinoJavaMembersBenchmark.base4ManyMember   thrpt   20   9.721 ± 0.218  ops/ms      1.000
RhinoJavaMembersBenchmark.base4SmallMember  thrpt   20  31.695 ± 0.236  ops/ms      3.260

JMH 1.21 (released 527 days ago, please consider updating!)
=============================================

ManyMember=200, SmallMember=1   Small vs Many > 5x faster

Benchmark                                    Mode  Cnt   Score   Error   Units  Score/min
RhinoJavaMembersBenchmark.base4ManyMember   thrpt   20   5.490 ± 0.037  ops/ms      1.000
RhinoJavaMembersBenchmark.base4SmallMember  thrpt   20  31.148 ± 1.159  ops/ms      5.673

===================================
Benchmark                                   Mode  Cnt   Score   Error   Units  Score/min
RhinoJavaMembersBenchmark.base4Member001p  thrpt   20  32.970 ± 0.186  ops/ms      6.120
RhinoJavaMembersBenchmark.base4Member010p  thrpt   20  27.672 ± 0.172  ops/ms      5.136
RhinoJavaMembersBenchmark.base4Member020p  thrpt   20  24.430 ± 0.196  ops/ms      4.535
RhinoJavaMembersBenchmark.base4Member050p  thrpt   20  14.620 ± 1.791  ops/ms      2.714
RhinoJavaMembersBenchmark.base4Member100p  thrpt   20   9.254 ± 0.118  ops/ms      1.718
RhinoJavaMembersBenchmark.base4Member200p  thrpt   20   5.387 ± 0.054  ops/ms      1.000

rhino 1.7.7.2

====================================
Benchmark                                   Mode  Cnt   Score   Error   Units  Score/min
RhinoJavaMembersBenchmark.base4Member001p  thrpt   20  34.578 ± 0.267  ops/ms      6.567
RhinoJavaMembersBenchmark.base4Member010p  thrpt   20  27.703 ± 1.591  ops/ms      5.262
RhinoJavaMembersBenchmark.base4Member020p  thrpt   20  22.040 ± 2.076  ops/ms      4.186
RhinoJavaMembersBenchmark.base4Member050p  thrpt   20  15.151 ± 0.276  ops/ms      2.878
RhinoJavaMembersBenchmark.base4Member100p  thrpt   20   9.330 ± 0.161  ops/ms      1.772
RhinoJavaMembersBenchmark.base4Member200p  thrpt   20   5.265 ± 0.050  ops/ms      1.000

rhino 1.7.11

=============

Benchmark                                   Mode  Cnt   Score   Error   Units  Score/min
RhinoJavaMembersBenchmark.base4Member001p  thrpt   20  54.640 ± 0.904  ops/ms      3.993
RhinoJavaMembersBenchmark.base4Member010p  thrpt   20  46.350 ± 1.426  ops/ms      3.387
RhinoJavaMembersBenchmark.base4Member020p  thrpt   20  42.011 ± 0.541  ops/ms      3.070
RhinoJavaMembersBenchmark.base4Member050p  thrpt   20  30.543 ± 0.431  ops/ms      2.232
RhinoJavaMembersBenchmark.base4Member100p  thrpt   20  21.536 ± 0.467  ops/ms      1.574
RhinoJavaMembersBenchmark.base4Member200p  thrpt   20  13.683 ± 0.106  ops/ms      1.000

===============

Benchmark                                              Mode  Cnt   Score   Error   Units  Score/min
RhinoJavaMembersBenchmark.base4Member001p             thrpt   20  48.937 ± 4.015  ops/ms      3.985
RhinoJavaMembersBenchmark.base4Member010p             thrpt   20  47.803 ± 0.720  ops/ms      3.893
RhinoJavaMembersBenchmark.base4Member020p             thrpt   20  34.743 ± 2.285  ops/ms      2.829
RhinoJavaMembersBenchmark.base4Member050p             thrpt   20  28.549 ± 1.485  ops/ms      2.325
RhinoJavaMembersBenchmark.base4Member100p             thrpt   20  18.308 ± 1.542  ops/ms      1.491
RhinoJavaMembersBenchmark.base4Member200p             thrpt   20  12.523 ± 1.000  ops/ms      1.020
RhinoJavaMembersBenchmark.lazyInitOff4Member050p      thrpt   20  28.766 ± 1.326  ops/ms      2.342
RhinoJavaMembersBenchmark.lazyInitOn4Member050p       thrpt   20  68.453 ± 1.650  ops/ms      5.574
RhinoJavaMembersBenchmark.old4Member050p              thrpt   20  14.984 ± 0.414  ops/ms      1.220
RhinoJavaMembersBenchmark.reflectCacheOff4Member050p  thrpt   20  12.280 ± 0.687  ops/ms      1.000

===============================
Benchmark                                              Mode  Cnt   Score   Error   Units  Score/min
RhinoJavaMembersBenchmark.base4Member001p             thrpt   20  75.595 ± 0.648  ops/ms      5.245
RhinoJavaMembersBenchmark.base4Member010p             thrpt   20  74.497 ± 1.710  ops/ms      5.169
RhinoJavaMembersBenchmark.base4Member020p             thrpt   20  69.359 ± 7.501  ops/ms      4.812
RhinoJavaMembersBenchmark.base4Member050p             thrpt   20  66.918 ± 2.661  ops/ms      4.643
RhinoJavaMembersBenchmark.base4Member100p             thrpt   20  67.920 ± 1.336  ops/ms      4.712
RhinoJavaMembersBenchmark.base4Member200p             thrpt   20  66.287 ± 1.537  ops/ms      4.599
RhinoJavaMembersBenchmark.lazyInitOff4Member050p      thrpt   20  28.667 ± 0.825  ops/ms      1.989
RhinoJavaMembersBenchmark.lazyInitOn4Member050p       thrpt   20  68.870 ± 1.514  ops/ms      4.778
RhinoJavaMembersBenchmark.old4Member050p              thrpt   20  14.414 ± 1.768  ops/ms      1.000
RhinoJavaMembersBenchmark.reflectCacheOff4Member050p  thrpt   20  18.117 ± 1.411  ops/ms      1.257


*/
}
