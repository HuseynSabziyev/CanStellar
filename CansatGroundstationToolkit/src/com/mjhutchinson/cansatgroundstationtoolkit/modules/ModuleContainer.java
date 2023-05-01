package com.mjhutchinson.cansatgroundstationtoolkit.modules;

import java.util.Enumeration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Michael Hutchinson on 01/11/2014 at 20:29.
 *
 * Defines a container for the modules registered to the program.
 */
public class ModuleContainer{

    private ConcurrentHashMap<String, AbstractModule> moduleConcurrentHashMap = new ConcurrentHashMap<>();

    /**
     * @param key the key to search for in the ConcurrentHashMap.
     * @return the value associated with the key.
     */
    public AbstractModule getValue(String key){
        return moduleConcurrentHashMap.get(key);
    }

    /**
     * Puts an AbstractModule into the ConcurrentHashMap.
     *
     * @param module the module to register.
     * @throws NullPointerException if the key or value is null.
     */
    public void putValue(AbstractModule module){
        moduleConcurrentHashMap.put(module.getTITLE(), module);
    }

    /**
     * Gets the Enumeration of the elements in the ConcurrentHashMap.
     *
     * @return the Enumeration of values.
     */
    public Enumeration<AbstractModule> getElements(){
        return moduleConcurrentHashMap.elements();
    }

    /**
     * Gets the keyset from the ConcurrentHashMap.
     *
     * @return a Set of type String containing the keys from the ConcurrentHashMap.
     */
    public Set<String> keySet(){
        return moduleConcurrentHashMap.keySet();
    }

    /**
     * Attempts to remove a particular value from the ConcurrentHashMap.
     *
     * @param module the AbstractModule to be removed.
     * @throws NullPointerException if the module is null.
     */
    public void removeValue(AbstractModule module){
        moduleConcurrentHashMap.remove(module.getTITLE());
    }

}
