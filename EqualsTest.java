package com.equals;

import org.junit.Test;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import sun.misc.Unsafe;

public class EqualsTest {


    private static Unsafe unsafe;
 
    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long addressOf(Object o) throws Exception {  
        
        Object[] array = new Object[] { o };  
  
        long baseOffset = unsafe.arrayBaseOffset(Object[].class);  
        int addressSize = unsafe.addressSize();  
        long objectAddress;  
        switch (addressSize) {  
        case 4:  
            objectAddress = unsafe.getInt(array, baseOffset);  
            break;  
        case 8:  
            objectAddress = unsafe.getLong(array, baseOffset);  
            break;  
        default:  
            throw new Error("unsupported address size: " + addressSize);  
        }  
        return (objectAddress);  
    }

	public static void main(String[] args) throws Exception {
		Student s1 = new Student();
		s1.setId("1");
		s1.setName("mike");
		Student s2 = new Student();
		s2.setId("1");
		s2.setName("mike");
		Student s3 = s1;
		System.out.println(addressOf(s1));
		System.out.println(addressOf(s3));
		System.out.println(s1);
		System.out.println(s1.hashCode());
		new Thread(() -> {
			for (int i = 0; i < 10000; i++) {
				s1.setId(String.valueOf(Math.random()));
				System.out.println(s1.hashCode());
			}
		}).start();
		Set<Student> set = new HashSet<>();
		Runtime.getRuntime().gc();
	}

	@Test
	public void testHC() {
		System.out.println("fss".hashCode());
	}
}
