
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
    public Object old4Member001p(Blackhole bh) {
        return runJsOnVo(bh, new Member1pBean() , Holder4JavaMembersOld.INST);
    }
   
   @Benchmark
   public Object old4Member100p(Blackhole bh) {
       return runJsOnVo(bh, new Member100pBean() , Holder4JavaMembersOld.INST);
   }
   
   @Benchmark
   public Object old4Member200p(Blackhole bh) {
       return runJsOnVo(bh, new Member200pBean() , Holder4JavaMembersOld.INST);
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
===================================
Benchmark                                   Mode  Cnt   Score   Error   Units  Score/min
RhinoJavaMembersBenchmark.base4Member001p  thrpt   20  32.970 ± 0.186  ops/ms      6.120
RhinoJavaMembersBenchmark.base4Member010p  thrpt   20  27.672 ± 0.172  ops/ms      5.136
RhinoJavaMembersBenchmark.base4Member020p  thrpt   20  24.430 ± 0.196  ops/ms      4.535
RhinoJavaMembersBenchmark.base4Member050p  thrpt   20  14.620 ± 1.791  ops/ms      2.714
RhinoJavaMembersBenchmark.base4Member100p  thrpt   20   9.254 ± 0.118  ops/ms      1.718
RhinoJavaMembersBenchmark.base4Member200p  thrpt   20   5.387 ± 0.054  ops/ms      1.000


rhino 1.7.11
====================================
Benchmark                                              Mode  Cnt   Score   Error   Units  Score/min
RhinoJavaMembersBenchmark.base4Member001p             thrpt   20  60.352 ± 1.670  ops/ms     14.026
RhinoJavaMembersBenchmark.base4Member010p             thrpt   20  59.010 ± 1.718  ops/ms     13.714
RhinoJavaMembersBenchmark.base4Member020p             thrpt   20  56.018 ± 1.780  ops/ms     13.019
RhinoJavaMembersBenchmark.base4Member050p             thrpt   20  56.259 ± 2.028  ops/ms     13.075
RhinoJavaMembersBenchmark.base4Member100p             thrpt   20  56.998 ± 1.300  ops/ms     13.247
RhinoJavaMembersBenchmark.base4Member200p             thrpt   20  54.377 ± 2.047  ops/ms     12.637
RhinoJavaMembersBenchmark.lazyInitOff4Member050p      thrpt   20  21.301 ± 1.565  ops/ms      4.950
RhinoJavaMembersBenchmark.lazyInitOn4Member050p       thrpt   20  57.212 ± 1.699  ops/ms     13.296
RhinoJavaMembersBenchmark.old4Member001p              thrpt   20  24.235 ± 0.553  ops/ms      5.632
RhinoJavaMembersBenchmark.old4Member050p              thrpt   20  11.430 ± 0.163  ops/ms      2.656
RhinoJavaMembersBenchmark.old4Member100p              thrpt   20   7.230 ± 0.048  ops/ms      1.680
RhinoJavaMembersBenchmark.old4Member200p              thrpt   20   4.303 ± 0.038  ops/ms      1.000
RhinoJavaMembersBenchmark.reflectCacheOff4Member050p  thrpt   20  14.271 ± 1.371  ops/ms      3.317


rhino  1.7.7.2
==================================
Benchmark                                              Mode  Cnt   Score   Error   Units  Score/min
RhinoJavaMembersBenchmark.base4Member001p             thrpt   20  58.649 ± 0.272  ops/ms     13.180
RhinoJavaMembersBenchmark.base4Member010p             thrpt   20  57.837 ± 0.323  ops/ms     12.998
RhinoJavaMembersBenchmark.base4Member020p             thrpt   20  57.664 ± 0.216  ops/ms     12.959
RhinoJavaMembersBenchmark.base4Member050p             thrpt   20  57.913 ± 0.168  ops/ms     13.015
RhinoJavaMembersBenchmark.base4Member100p             thrpt   20  57.923 ± 0.605  ops/ms     13.017
RhinoJavaMembersBenchmark.base4Member200p             thrpt   20  57.063 ± 0.784  ops/ms     12.824
RhinoJavaMembersBenchmark.lazyInitOff4Member050p      thrpt   20  25.693 ± 0.381  ops/ms      5.774
RhinoJavaMembersBenchmark.lazyInitOn4Member050p       thrpt   20  56.958 ± 1.618  ops/ms     12.800
RhinoJavaMembersBenchmark.old4Member001p              thrpt   20  26.867 ± 0.202  ops/ms      6.038
RhinoJavaMembersBenchmark.old4Member050p              thrpt   20  12.091 ± 0.139  ops/ms      2.717
RhinoJavaMembersBenchmark.old4Member100p              thrpt   20   7.588 ± 0.077  ops/ms      1.705
RhinoJavaMembersBenchmark.old4Member200p              thrpt   20   4.450 ± 0.026  ops/ms      1.000
RhinoJavaMembersBenchmark.reflectCacheOff4Member050p  thrpt   20  15.193 ± 0.148  ops/ms      3.414
*/
}
