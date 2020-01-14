////////////////////////////////////////////////////////////////////////////////
// Main File:        BookHashTable.java
// This File:        DS_My.java
// Other Files:      BookHashTable.java, BookHashTableTest.java, BookParser.java, Book.java
// Semester:         CS 400 Fall 2019
//
// Author:           Yeochan Youn
// Email:            yyoun5@wisc.edu
// CS Login:         yeochan
//
/////////////////////////// OTHER SOURCES OF HELP //////////////////////////////
//                   fully acknowledge and credit all sources of help,
//                   other than Instructors and TAs.
//
// Persons:          Identify persons by name, relationship to you, and email.
//                   Describe in detail the the ideas and help they provided.
//
// Online sources:   avoid web searches to solve your problems, but if you do
//                   search, be sure to include Web URLs and description of 
//                   of any information you find.
//////////////////////////// 80 columns wide ///////////////////////////////////

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

// TODO: comment and complete your HashTableADT implementation
//
// TODO: implement all required methods
// DO ADD REQUIRED PUBLIC METHODS TO IMPLEMENT interfaces
//
// DO NOT ADD ADDITIONAL PUBLIC MEMBERS TO YOUR CLASS 
// (no public or package methods that are not in implemented interfaces)
//
// TODO: describe the collision resolution scheme you have chosen
// identify your scheme as open addressing or bucket
//
// if open addressing: describe probe sequence 
// if buckets: describe data structure for each bucket
//
// TODO: explain your hashing algorithm here 
// NOTE: you are not required to design your own algorithm for hashing,
//       since you do not know the type for K,
//       you must use the hashCode provided by the <K key> object




// Collision resolution: open addressing, linear probe
// when addressing, if that index is allocated, move to next index until it find open address which is null

/** HashTable implementation that uses:
 * @param <K> unique comparable identifier for each <K,V> pair, may not be null
 * @param <V> associated value with a key, value may be null
 */
public class BookHashTable implements HashTableADT<String, Book> {

    /** The initial capacity that is used if none is specifed user */
    static final int DEFAULT_CAPACITY = 101;
    
    /** The load factor that is used if none is specified by user */
    static final double DEFAULT_LOAD_FACTOR_THRESHOLD = 0.75;
    
    private KVpair[] hashTable;
    private double loadFactorThreshold;
    private int numKeys;
    
    private class KVpair{ // object which contains key and value separately
    	String key;
    	Book value;
    	private KVpair(String key, Book value) { // constructor of KVpair object
    		this.key = key;
    		this.value = value;
    	}

		private String getKey() { // return key of KVpair 
			return this.key;
		}

		private Book getVal() { // return value of KVpair
			return this.value;
		}

    }
    /**
     * REQUIRED default no-arg constructor
     * Uses default capacity and sets load factor threshold 
     * for the newly created hash table.
     */
    public BookHashTable() {
        this(DEFAULT_CAPACITY,DEFAULT_LOAD_FACTOR_THRESHOLD);
    }
    
    /**
     * Creates an empty hash table with the specified capacity 
     * and load factor.
     * @param initialCapacity number of elements table should hold at start.
     * @param loadFactorThreshold the ratio of items/capacity that causes table to resize and rehash
     */
    public BookHashTable(int initialCapacity, double loadFactorThreshold) {
        // TODO: comment and complete a constructor that accepts initial capacity 
        // and load factor threshold and initializes all fields
    	hashTable = new KVpair[initialCapacity];
    	this.loadFactorThreshold = loadFactorThreshold;
    }

	@Override
    // Add the key,value pair to the data structure and increase the number of keys.
    // If key is null, throw IllegalNullKeyException;
    // If key is already in data structure, throw DuplicateKeyException();
	public void insert(String key, Book value) throws IllegalNullKeyException, DuplicateKeyException {
		if(key == null)throw new IllegalNullKeyException(); // throw IllegalNullKeyException if key is null
		if(contain(key)) throw new DuplicateKeyException(); // throw DuplicateKeyException if key is already in hashTable
		
		KVpair KV = new KVpair(key, value); // new KVpair for inserting key and value
		int i = 0;
		while(true) { // run until break
			int ind = (int)((Math.abs(key.hashCode())+i) % hashTable.length); // finding proper address by linear probe sequence
			if(hashTable[ind] == null) { // if address is open, insert KV in that index
				hashTable[ind] = KV;
				break; // break the loop
			}
			i++; // if index is allocated, move to next index. Increasing index by 1
		}
		numKeys++; // if inserted, increase numKeys by 1
		if(getLoadFactorThreshold() >= loadFactorThreshold) { hashTable = resize(hashTable);} // if loadfactor is equal or greater than threshold, resize hashTable
	}
	
	/**
	 * 
	 * @param hashTable hashTable which needs to be resized
	 * @return newTable return resized table
	 * @throws IllegalNullKeyException
	 * @throws DuplicateKeyException
	 */
	
	private KVpair[] resize(KVpair[] hashTable) throws IllegalNullKeyException, DuplicateKeyException { 
		KVpair[] newTable = new KVpair[hashTable.length * 2 + 1]; // create new table which is double and + 1 from original table
		
		for (int i = 0; i < hashTable.length; i++) { // visiting all index of original table
			if (hashTable[i] != null) { // if current index is not null, move key to new table
				int j = 0;
				while (true) { // run until break
					int ind = (int) ((Math.abs(hashTable[i].getKey().hashCode()) + j) % newTable.length); // find new hash index which corresponds to new table size
					if (newTable[ind] == null) { // if current address is open, insert key from original table to that address
						newTable[ind] = hashTable[i];
						break;
					} 
					j++; // else move to next index, which is one bigger than current address
				}
			}
		}
		return newTable; // return resized hashTable
	}
	
	/**
	 * 
	 * @param key key to find
	 * @return true if contains, false otherwise
	 */
	private boolean contain(String key) {
		for(int i = 0; i < hashTable.length; i++) { // run through the table
			if(hashTable[i] != null) { // if current index is not null, compare to target key
			if(hashTable[i].getKey() == key) {return true;} // if key found, return true
			}
		}
		return false; // return false is key is not found
	}

	@Override
    // If key is found, 
    //    remove the key,value pair from the data structure
    //    decrease number of keys.
    //    return true
    // If key is null, throw IllegalNullKeyException
    // If key is not found, return false
	public boolean remove(String key) throws IllegalNullKeyException {
		if(key == null) throw new IllegalNullKeyException(); // if key is null throw IllegalNullKeyException
		if(!contain(key)) return false; // if key does not exist, return false
		
		int i = 0;
		while(true) { // runt until break
			int j = ((Math.abs(key.hashCode())+i)) % hashTable.length; // starting from hash index
			if(hashTable[j] != null) { // if index is not null, compare with removing key
			if(hashTable[j].getKey().equals(key)) { // if key is found, set it to null and decrease numKeys by 1, and return true
				hashTable[j] = null;
				numKeys--;
				return true;
			}
			}
			{i++;} // if key does not match, move to next index
		}
	}

	@Override
    // Returns the value associated with the specified key
    // Does not remove key or decrease number of keys
    //
    // If key is null, throw IllegalNullKeyException 
    // If key is not found, throw KeyNotFoundException().
	public Book get(String key) throws IllegalNullKeyException, KeyNotFoundException {
		if (key == null) {throw new IllegalNullKeyException();} // throw IllegalNullKeyException if key is null
		for (int i = 0; i < hashTable.length; i++) { // run through hashTable
			if (hashTable[i] != null) { // if hashTable is not null compare with target key
				if (hashTable[i].getKey() == key) { // if key is found, return corresponding value
					return hashTable[i].getVal();
				}
			}
		}
		throw new KeyNotFoundException(); // if key not found, throw KeyNotFoundException
	}

	@Override
    // Returns the number of key,value pairs in the data structure
	public int numKeys() {
		return numKeys; // return numKeys
	}

	@Override
    // Notice:
    // THIS INTERFACE EXTENDS AND INHERITS ALL METHODS FROM DataStructureADT
    // and adds the following operations:

    // Returns the load factor for this hash table
    // that determines when to increase the capacity 
    // of this hash table
	public double getLoadFactorThreshold() {
		double loadFactor;
		loadFactor = numKeys/hashTable.length; // divide numKeys by table length
		return loadFactor; // return loadFactor
	}

	@Override
    // Capacity is the size of the hash table array
    // This method returns the current capacity.
    //
    // The initial capacity must be a positive integer, 1 or greater
    // and is specified in the constructor.
    // 
    // REQUIRED: When the load factor is reached, 
    // the capacity must increase to: 2 * capacity + 1
    //
    // Once increased, the capacity never decreases
	public int getCapacity() {
		return hashTable.length; // return length of hashTable
	}

	@Override
    // Returns the collision resolution scheme used for this hash table.
    // Implement this ADT with one of the following collision resolution strategies
    // and implement this method to return an integer to indicate which strategy.
    //
     // 1 OPEN ADDRESSING: linear probe
     // 2 OPEN ADDRESSING: quadratic probe
     // 3 OPEN ADDRESSING: double hashing
     // 4 CHAINED BUCKET: array list of array lists
     // 5 CHAINED BUCKET: array list of linked lists
     // 6 CHAINED BUCKET: array list of binary search trees
     // 7 CHAINED BUCKET: linked list of array lists
     // 8 CHAINED BUCKET: linked list of linked lists
     // 9 CHAINED BUCKET: linked list of of binary search trees
	public int getCollisionResolutionScheme() {
		return 1; // return 1, which represents open addressing, linear probe
	}
		
    // TODO: add all unimplemented methods so that the class can compile

}