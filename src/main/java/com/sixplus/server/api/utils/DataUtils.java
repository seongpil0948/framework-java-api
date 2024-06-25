package com.sixplus.server.api.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.*;

/**
 * [NOTE]
 * DataUtils은 기본 JDK만 종속성을 가지며 다른 SDK와 종속성을 가질 수 없음
 * 또한 내부 패키지와 종속성 가질 수 없으며 독립적으로 실행해야 함
 * ex) CmmUtils에서는 DataUtils을 사용할 수 있지만, DataUtils는 CmmUtils 사용불가
 */
@Slf4j
public class DataUtils {
	public static final String DEFAULT_KEY_SOURCE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; 
	public static <T> void stream(List<T> data) {}
	
	public static <T> boolean isEmpty(List<T> obj) {
		return (null == obj || obj.isEmpty());
	}
	
	public static <T> boolean isEmpty(Collection<T> obj) {
		return (null == obj || obj.isEmpty());
	}

	public static <K, V> boolean isEmpty(Map<K, V> obj) {
		return (null == obj || obj.isEmpty());
	}
	
	public static <K, V> String defaultMapToString(Object obj) {
		String ret = "";
		if (null != obj ) {
			if (obj instanceof String ) {
				ret = (String)obj;
			}else if (obj instanceof BigDecimal ) {
				ret = String.valueOf((BigDecimal)obj);
			}else if (obj instanceof Integer ) {
				ret = String.valueOf((int)obj);
			}else {
				log.warn("Casting error!!");
			}
		}
		return ret;
	}
	public static <K, V> String defaultMapToString(Object obj, String default_str) {
		String ret = "";
		if (null != obj ) {
			if (obj instanceof String ) {
				ret = (String)obj;
			}else if (obj instanceof BigDecimal ) {
				ret = String.valueOf((BigDecimal)obj);
			}else if (obj instanceof Integer ) {
				ret = String.valueOf((int)obj);
			}else {
				log.warn("Casting error!!");
			}
		}else {
			ret = default_str;
		}
		return ret;
	}

	public static <T> boolean isEmpty(Set<T> obj) {
		return (null == obj || obj.isEmpty());
	}

	public static <T> boolean isEmpty(T[] obj) {
		return (null == obj || 0 == obj.length);
	}
	
	public static <T> List<List<T>> combination(List<List<T>> dataSet) {
		return DataUtils.reduce(dataSet, DataUtils::combination);
	}

	public static <T> List<List<T>> combination(List<List<T>> ret, List<T> values) {
		if(isEmpty(ret)) {
			return DataUtils.reduce(values, (r,v)->r.add(new ArrayList<>(Arrays.asList(v))), new ArrayList<>());
		}
		
		return DataUtils.reduce(ret, (r,d)->DataUtils.iterate(values, v->r.add(DataUtils.add(d, v))), new ArrayList<>());
	}

	public static <T> List<List<T>> transform(List<List<T>> dataSet) {
		int max_size = maxSize(dataSet);
		List<List<T>> ret = new ArrayList<>();
		for(int i=0; i<max_size; ++i) ret.add(new ArrayList<>());
		
		for(List<T> it : dataSet) {
			DataUtils.zip(ret, it, (r,v)->r.add(v));
		}
		
		return ret;
	}

	public static <T> List<T> add(List<T> l, T r) {
		List<T> ret = new ArrayList<>(l);
		ret.add(r);
		
		return ret;
	}

	public static <T> List<T> add(List<T> l, List<T> r) {
		if (isEmpty(l)) return r;
		if (isEmpty(r)) return l;
		
		List<T> ret = new ArrayList<>(l);
		ret.addAll(r);
		
		return ret;
	}

	public static <T,U> void zip(List<T> data1, List<U> data2, BiConsumer<T,U> action) {
		if (DataUtils.isEmpty(data1) || DataUtils.isEmpty(data2)) return;
		Iterator<T> it1 = data1.iterator();
		Iterator<U> it2 = data2.iterator();
		while(it1.hasNext() && it2.hasNext()) {
			action.accept(it1.next(), it2.next());
		}
	}
	
	public static <T,U> void zipUp(List<T> data1, List<U> data2, BiConsumer<T,U> action) {
		if(null == data1 || null == data2) return;
		
		Iterator<T> it1 = data1.iterator();
		Iterator<U> it2 = data2.iterator();
		while(it1.hasNext() || it2.hasNext()) {
			action.accept(it1.hasNext()?it1.next():null, it2.hasNext()?it2.next():null);
		}
	}
	
	public static <T> void iterate(Collection<T> array, Consumer<T> action) {
		if(isEmpty(array)) return;
		for(T it: array) action.accept(it);
	}

	public static <T> void iterate(T[] array, Consumer<T> action) {
		if (isEmpty(array)) return;
		for(T it: array) action.accept(it);
	}
	
	public static <T> void iterate(T[][] array, Consumer<T[]> action) {
		if (isEmpty(array)) return;
		for(T[] it: array) action.accept(it);
	}
	
	public static <T> void until(Collection<T> array, Predicate<T> action) {
		if(isEmpty(array)) return;
		for(T it: array) if(action.test(it)) break;
	}
	
	public static <T> void until(T[] array, Predicate<T> action) {
		if(isEmpty(array)) return;
		for(T it: array) if(action.test(it)) break;
	}
	
	public static <T> boolean some(List<T> array, Predicate<T> filter) {
		if (isEmpty(array)) return false;
		for(T it: array) if(filter.test(it)) return true;
		return false;
	}
	
	public static <T> boolean some(T[] array, Predicate<T> filter) {
		if (isEmpty(array)) return false;
		for(T it: array) if(filter.test(it)) return true;
		return false;
	}
	
	public static <T> boolean every(List<T> array, Predicate<T> filter) {
		if (isEmpty(array)) return false;
		for(T it: array) if(!filter.test(it)) return false;
		return true;
	}
	
	public static <T> boolean every(T[] array, Predicate<T> filter) {
		if (isEmpty(array)) return false;
		for(T it: array) if(!filter.test(it)) return false;
		return true;
	}

	public static <T> T findFirst(List<T> array, Predicate<T> filter) {
		if (isEmpty(array)) return null;
		for(T it: array) if(filter.test(it)) return it;
		return null;
	}

	public static <T> T findFirst(T[] array, Predicate<T> filter) {
		if (isEmpty(array)) return null;
		T tmpIt = null;
		for(T it: array) if(filter.test(it)) {tmpIt =  it; break;}
		return tmpIt;
	}
	
	public static <T> T findFirst(Collection<T> array, Predicate<T> filter) {
		if (isEmpty(array)) return null;
		T tmpIt = null;
		for(T it: array) if(filter.test(it)) {tmpIt =  it; break;}
		return tmpIt;
	}

	//필터 조건에 맞는 대상은 제거됨
	public static <T> List<T> filter(List<T> array, Predicate<T> filter) {
		if (isEmpty(array)) return null;
		List<T> ret = new ArrayList<>();
		for(T it: array) if(!filter.test(it)) ret.add(it);
		return ret;
	}

	//필터 조건에 맞는 대상은 제거됨
	public static <T> List<T> filter(T[] array, Predicate<T> filter) {
		if (isEmpty(array)) return null;
		List<T> ret = new ArrayList<>();
		for(T it: array) if(!filter.test(it)) ret.add(it);
		return ret;
	}
	
	public static <R, T> List<R> map(Collection<T> array, Function<T, R> action) {
		if (isEmpty(array)) return null;
		List<R> ret = new ArrayList<>();
		for(T it: array) ret.add(action.apply(it));
		return ret;
	}

	public static <R, T> List<R> map(Set<T> array, Function<T, R> action) {
		if (isEmpty(array)) return null;
		List<R> ret = new ArrayList<>();
		for(T it: array) ret.add(action.apply(it));
		return ret;
	}

	public static <R, T> List<R> map(T[] array, Function<T, R> action) {
		if (isEmpty(array)) return null;
		List<R> ret = new ArrayList<>();
		for(T it: array) ret.add(action.apply(it));
		return ret;
	}
	
	public static <T> List<String> map(T[] array, Function<T, String> classNm, Function<T, String> methodNm, String sep) {
		if (isEmpty(array)) return null;
		List<String> ret = new ArrayList<>();
		for(T it1: array) ret.add(classNm.apply(it1) + sep + methodNm.apply(it1));
		return ret;
	}

	public static <T> List<String> map(T[] array, Function<T, String> classNm, Function<T, String> methodNm, String sep, String strLimit) {
		if (isEmpty(array)) return null;
		List<String> ret = new ArrayList<>();
		
		String tempClassNm = null;
		for(T it: array) {
			tempClassNm = classNm.apply(it);
			if(-1 < tempClassNm.indexOf("java.lang.Thread")) continue;
			if(-1 < tempClassNm.indexOf(strLimit)) break;
			ret.add(tempClassNm + sep + methodNm.apply(it));
		}
		
		Collections.reverse(ret);
		
		return ret;
	}
	
	public static <K, V, T> Map<K,V> map(Collection<T> array, Function<T, K> key, Function<T, V> value) {
		if (isEmpty(array)) return null;
		Map<K,V> ret = new HashMap<>();
		for(T it: array) ret.put(key.apply(it), value.apply(it));
		return ret;
	}

	public static <K, V, T> Map<K,V> map(T[] array, Function<T, K> key, Function<T, V> value) {
		if (isEmpty(array)) return null;
		Map<K,V> ret = new HashMap<>();
		for(T it: array) ret.put(key.apply(it), value.apply(it));
		return ret;
	}
	
	public static <K, T> Map<K, List<T>> partitioning(List<T> array, Function<T, K> key) {
		if (isEmpty(array)) return null;
		Map<K, List<T>> ret = new HashMap<>();
		for(T it: array) multimapAdd(ret, key.apply(it), it);
		return ret;
	}
	
	public static <K, T> Map<K, List<T>> partitioning(Set<T> array, Function<T, K> key) {
		if (isEmpty(array)) return null;
		Map<K, List<T>> ret = new HashMap<>();
		for(T it: array) multimapAdd(ret, key.apply(it), it);
		return ret;
	}
	
	public static <K, T> Map<K, List<T>> partitioning(T[] array, Function<T, K> key) {
		if (isEmpty(array)) return null;
		Map<K, List<T>> ret = new HashMap<>();
		for(T it: array) multimapAdd(ret, key.apply(it), it);
		return ret;
	}
	
	public static <K,T> void multimapAdd(Map<K, List<T>> data, K key, T value) {
		List<T> found = data.get(key);
		if(null == found) {
			data.put(key, found = new ArrayList<>());
		}
		found.add(value);
	}

	public static <R, T> R reduce(T[] array, BiConsumer<R,T> action, R init) {
		if (isEmpty(array)) return init;
		for(T it: array) action.accept(init, it);
		return init;
	}

	public static <R, T> R reduce(Collection<T> array, BiConsumer<R,T> action, R init) {
		if (isEmpty(array)) return init;
		for(T it: array) action.accept(init, it);
		return init;
	}

	public static <R, T> R reduce(Set<T> array, BiConsumer<R,T> action, R init) {
		if (isEmpty(array)) return init;
		for(T it: array) action.accept(init, it);
		return init;
	}

	public static <R, T> R reduce(T[] array, BiFunction<R,T,R> action) {
		if (isEmpty(array)) return null;
		R ret = null;
		for(T it: array) ret = action.apply(ret, it);
		return ret;
	}

	public static <R, T> R reduce(Collection<T> array, BiFunction<R,T,R> action) {
		if (isEmpty(array)) return null;
		R ret = null;
		for(T it: array) ret = action.apply(ret, it);
		return ret;
	}
	
	public static Integer[] sequence(int num) {
		return sequence(0, num);
	}
	
	public static Integer[] sequence(int begin, int end) {
		int size = Math.abs(end-begin);
		Integer[] ret = new Integer[size];
		
		if(begin<end) {
			for(int i = 0; i < size; ++i) {
				ret[i] = begin + i;
			}
		} else {
			for(int i = 0; i < size; ++i) {
				ret[i] = begin - i;
			}
		}
		
		return ret;
	}
	
	public static String getPasswordEncode(String key) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(key);
	}
	
	/**
	 * 랜덤키 생성 DEFAULT_KEY_SOURCE 이용함 
	 * @param size
	 * @return
	 */
	public static String generateKey(int size) {
		return generateKey(size, DEFAULT_KEY_SOURCE);
	}
	
	/**
	 * 랜덤키 생성 keySource 이용함 
	 * @param size
	 * @param keySource
	 * @return
	 */
	public static String generateKey(int size, String keySource) {
		try {
			SecureRandom secureRandom = SecureRandom.getInstanceStrong();

            return secureRandom
					.ints(size, 0, keySource.length())
					.mapToObj(keySource::charAt)
					.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
					.toString();
		} catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
	}
	
	public static <T> int size(List<T> list) {
		return isEmpty(list) ? 0 : list.size();
	}
	
	public static <T> int maxSize(List<List<T>> list) {
		if (isEmpty(list)) return 0;
		int max_size = 0;
		for(List<T> it : list) {
			if(null == it) continue;
			max_size = Math.max(max_size, it.size());
		}
		return max_size;
	}
	
	public static <T extends Comparable<T>> boolean between(T begin, T end, T date) {
		if (null == date) return false;
		
		if (null != begin && 0 > date.compareTo(begin)) {
			return false;
		}
		
		if (null != end && 0 < date.compareTo(end)) {
			return false;
		}
		
		return true;
	}
	
	public static Map<String, Object> introspectWithExcept(Object vo, String[] arrExceptList) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		BeanInfo info = Introspector.getBeanInfo(vo.getClass());
		for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
			Method reader = pd.getReadMethod();
			if (reader != null) {
				if (arrExceptList != null && arrExceptList.length > 0 && isContain(arrExceptList, pd.getName()))
					continue;
				result.put(pd.getName(), reader.invoke(vo));
			}
		}
		return result;
	}

	public static Boolean isContain(String[] arrList, String name) {
		for (String arr : arrList) {
			if (StringUtils.contains(arr, name))
				return true;
		}
		return false;
	}
	
}