package cz.skymia.cyrilrc.server.util

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

class ListWithWeight<T> {

	ConcurrentHashMap<T, Long> map = new ConcurrentHashMap();
	long sum = 0;
	def Random random = new Random()
	
	Logger log =  LoggerFactory.getLogger(ListWithWeight.class)
	
	synchronized void add(T object, long weight){
		map.put(object, weight)
		sum += weight
	}
	
	synchronized void remove(T object){
		def weight = map.remove(object)
		if( weight ){
			sum -= weight
		}
	}

	Iterator<T> randomIterator(count){
		return new RandomIterator(count)
	}
	
	List<T> randomList(count){
		def ret = new ArrayList<T>()
		for( T t: randomIterator(count)){
			ret.add(t)
		}
		Collections.shuffle(ret)
		return ret
	}

	class RandomIterator<T> implements Iterator<T>{

		Iterator<Map.Entry> iterator
		long count
		long restWeight
		long restCount
		
		RandomIterator(count){
			synchronized (ListWithWeight.this){
				this.count = count
				this.restWeight = sum
				this.iterator = map.entrySet().iterator()
				this.restCount = map.size()
			}	
		}
		
		@Override
		public boolean hasNext() {
			return count > 0 && iterator.hasNext();
		}

		@Override
		public T next() {
			while(true){
				Map.Entry entry = iterator.next()
				boolean choose;
				if( restCount <= count){
					choose = true
				} else{
					def pro = entry.value
					long rnd = (long) (random.nextDouble()*restWeight) 
					choose =  rnd < pro*count
					restWeight -= pro	 
				}
				
				restCount --;
				if( choose ){
					count--
					return entry.key
				}
			}
			return ;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("NOT IMPLEMENTED")
		}
		
	}
	
}
