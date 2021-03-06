/**
 * Filename:   TestHashTableDeb.java
 * Project:    p3
 * Authors:    Debra Deppeler (deppeler@cs.wisc.edu)
 * 
 * Semester:   Fall 2018
 * Course:     CS400
 * 
 * Due Date:   before 10pm on 10/29
 * Version:    1.0
 * 
 * Credits:    None so far
 * 
 * Bugs:       TODO: add any known bugs, or unsolved problems here
 */

import org.junit.After;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/** 
 * Test HashTable class implementation to ensure that required 
 * functionality works for all cases.
 */
public class BookHashTableTest {

    // Default name of books data file
    public static final String BOOKS = "books_clean.csv";

    // Empty hash tables that can be used by tests
    static BookHashTable bookObject;
    static ArrayList<Book> bookTable;

    static final int INIT_CAPACITY = 2;
    static final double LOAD_FACTOR_THRESHOLD = 0.49;
       
    static Random RNG = new Random(0);  // seeded to make results repeatable (deterministic)

    /** Create a large array of keys and matching values for use in any test */
    @BeforeAll
    public static void beforeClass() throws Exception{
        bookTable = BookParser.parse(BOOKS);
    }
    
    /** Initialize empty hash table to be used in each test */
    @BeforeEach
    public void setUp() throws Exception {
        // TODO: change HashTable for final solution
         bookObject = new BookHashTable(INIT_CAPACITY,LOAD_FACTOR_THRESHOLD);
    }

    /** Not much to do, just make sure that variables are reset     */
    @AfterEach
    public void tearDown() throws Exception {
        bookObject = null;
    }

    private void insertMany(ArrayList<Book> bookTable) 
        throws IllegalNullKeyException, DuplicateKeyException {
        for (int i=0; i < bookTable.size(); i++ ) {
            bookObject.insert(bookTable.get(i).getKey(), bookTable.get(i));
        }
    }
    
    private void removeMany(ArrayList<Book> bookTable) 
            throws IllegalNullKeyException, DuplicateKeyException {
            for (int i=0; i < bookTable.size(); i++ ) {
                bookObject.remove(bookTable.get(i).getKey());
            }
        }

    /** IMPLEMENTED AS EXAMPLE FOR YOU
     * Tests that a HashTable is empty upon initialization
     */
    @Test
    public void test000_collision_scheme() {
    	System.out.println(bookTable.get(0));
        if (bookObject == null)
        	fail("Gg");
    	int scheme = bookObject.getCollisionResolutionScheme();
        if (scheme < 1 || scheme > 9) 
            fail("collision resolution must be indicated with 1-9");
    }


    /** IMPLEMENTED AS EXAMPLE FOR YOU
     * Tests that a HashTable is empty upon initialization
     */
    @Test
    public void test000_IsEmpty() {
        //"size with 0 entries:"
        assertEquals(0, bookObject.numKeys());
    }

    /** IMPLEMENTED AS EXAMPLE FOR YOU
     * Tests that a HashTable is not empty after adding one (key,book) pair
     * @throws DuplicateKeyException 
     * @throws IllegalNullKeyException 
     */
    @Test
    public void test001_IsNotEmpty() throws IllegalNullKeyException, DuplicateKeyException {
    	bookObject.insert(bookTable.get(0).getKey(),bookTable.get(0));
        String expected = ""+1;
        //"size with one entry:"
        assertEquals(expected, ""+bookObject.numKeys());
    }
    
    /** IMPLEMENTED AS EXAMPLE FOR YOU 
    * Test if the hash table  will be resized after adding two (key,book) pairs
    * given the load factor is 0.49 and initial capacity to be 2.
    */
    
    @Test 
    public void test002_Resize() throws IllegalNullKeyException, DuplicateKeyException {
    	bookObject.insert(bookTable.get(0).getKey(),bookTable.get(0));
    	int cap1 = bookObject.getCapacity(); 
    	bookObject.insert(bookTable.get(1).getKey(),bookTable.get(1));
    	int cap2 = bookObject.getCapacity();
        //"size with one entry:"
        assertTrue(cap2 > cap1 & cap1 ==2);
    }
    
    /**
     * throw duplicate exception for inserting existing key
     * @throws IllegalNullKeyException
     * @throws DuplicateKeyException
     * @throws KeyNotFoundException
     */
    @Test
    public void test003_noDuplicate() throws IllegalNullKeyException, DuplicateKeyException, KeyNotFoundException {
    	bookObject.insert(bookTable.get(0).getKey(),bookTable.get(0));
    	bookObject.insert(bookTable.get(1).getKey(),bookTable.get(1));
    	try { 
    	bookObject.insert(bookTable.get(0).getKey(),bookTable.get(0)); // inserting duplicate key
    	fail("table should not allow duplicate key, but it did for: " + bookTable.get(0).getKey()); // fail if line passed
    	}catch(DuplicateKeyException e) {
    	} 
    }
    
    /**
     * when remove existing key and insert again, exception should not be thrown
     * @throws IllegalNullKeyException
     * @throws DuplicateKeyException
     */
    @Test
    public void test004_removeAndInsertAgain() throws IllegalNullKeyException, DuplicateKeyException {
    	bookObject.insert(bookTable.get(0).getKey(),bookTable.get(0));
    	bookObject.insert(bookTable.get(1).getKey(),bookTable.get(1));
    	bookObject.insert(bookTable.get(2).getKey(),bookTable.get(2));
    	bookObject.insert(bookTable.get(3).getKey(),bookTable.get(3));
    	bookObject.remove(bookTable.get(1).getKey()); // remove key 1
    	try {
    	bookObject.insert(bookTable.get(1).getKey(),bookTable.get(1)); // insert key 1 again
    	}catch(DuplicateKeyException e) {
    		fail("table should not throw DuplicateKeyException for removed key, but it did for: " + bookTable.get(1).getKey()); // if exception was thrown, fail
    	}
    }
    
    /**
     * remove should not change the capacity
     * @throws IllegalNullKeyException
     * @throws DuplicateKeyException
     */
    @Test
    public void test005_RemoveAndCapacity() throws IllegalNullKeyException, DuplicateKeyException{
    	insertMany(bookTable);
    	int cap1 = bookObject.getCapacity();
    	removeMany(bookTable);
    	int cap2 = bookObject.getCapacity();
    	if(cap1 != cap2) { // if capacity is changed, it is fail
    		fail("remove shold not change capacity" + cap1 + ", but it did to " + cap2);
    	}
    }
    
    /**
     * remove should return false for removing unexisting key
     * @throws IllegalNullKeyException
     * @throws DuplicateKeyException
     */
    @Test
    public void test006_RemoveUnexistKey() throws IllegalNullKeyException, DuplicateKeyException {
    	bookObject.insert(bookTable.get(0).getKey(),bookTable.get(0));
    	if(bookObject.remove(bookTable.get(1).getKey())) { // if true is returned for unexisting key, test fail
    		fail("remove should return for unexisting key , but it return true");
    	}
    }
    
    /**
     * inserting null key should throw IllegalNullKeyException
     * @throws IllegalNullKeyException
     * @throws DuplicateKeyException
     */
    @Test
    public void test007_insertNullKey() throws IllegalNullKeyException, DuplicateKeyException {
    	try {
    	bookObject.insert(null,bookTable.get(0));
    	fail("should throw IllegalNullKeyException for inserting null key, but it does not");
    	}catch(IllegalNullKeyException e) {		
    	}
    }
    
    /**
     * removing null key should throw IllegalNullKeyException
     * @throws IllegalNullKeyException
     * @throws DuplicateKeyException
     */
    @Test
    public void test008_removeNullKey() throws IllegalNullKeyException, DuplicateKeyException {
    	bookObject.insert(bookTable.get(0).getKey(),bookTable.get(0));
    	try {
    	bookObject.remove(null);
    	fail("should throw IllegalNullKeyException for removing null key, but it does not");
    	}catch(IllegalNullKeyException e) {		
    	}
    }
    
    /**
     * numkey is increasing by 1 whenever new key is inserted
     * @throws IllegalNullKeyException
     * @throws DuplicateKeyException
     */
    @Test
    public void test009_numKeysIncrement() throws IllegalNullKeyException, DuplicateKeyException {
    	for (int i=0; i < bookTable.size(); i++ ) {
            bookObject.insert(bookTable.get(i).getKey(), bookTable.get(i));
            if(bookObject.numKeys() != i+1) {
            	fail("numKey should be " + (i+1) + "but it is " + bookObject.numKeys());
            }
        }
    }
    
    /**
     * loadfactor threshold should not change when many keys are inserted
     * @throws IllegalNullKeyException
     * @throws DuplicateKeyException
     */
    @Test
    public void test010_getLoadFactorThresholdNotChange() throws IllegalNullKeyException, DuplicateKeyException {
    	double threshold = bookObject.getLoadFactorThreshold();
    	insertMany(bookTable);
    	double threshold2 = bookObject.getLoadFactorThreshold();
    	if(threshold != threshold2) {
    		fail("threshold should not change after inserting many keys, but it changed. Original threshold: " + threshold + "After insert: " + threshold2);
    	}
    }
    
    /**
     * correct value should be returned for corresponding key
     * @throws IllegalNullKeyException
     * @throws DuplicateKeyException
     * @throws KeyNotFoundException
     */
    @Test
    public void test011_getReturnsCorrectValue() throws IllegalNullKeyException, DuplicateKeyException, KeyNotFoundException {
    	bookObject.insert(bookTable.get(0).getKey(),bookTable.get(0));
    	bookObject.insert(bookTable.get(1).getKey(),bookTable.get(1));
    	bookObject.insert(bookTable.get(2).getKey(),bookTable.get(2));
    	if(bookObject.get(bookTable.get(0).getKey()) != bookTable.get(0)) {
    		fail("get should return " + bookTable.get(0) + "but it returns " + bookObject.get(bookTable.get(0).getKey()));
    	}
    }
    
    @Test
    public void test111_insert_Many() throws IllegalNullKeyException, DuplicateKeyException {
    	insertMany(bookTable);
    }
    
}
