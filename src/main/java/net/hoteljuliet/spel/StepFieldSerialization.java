package net.hoteljuliet.spel;

import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * A Wrapper class which should be extended to handle Step attributes that are not serializable.
 * There are 6 cases to consider:
 * 1) the Step field IS serializable and DOES implement Serializable - nothing more to do.
 * 2) the Step field IS NOT serializable and DOES NOT implement Serializable OR
 *    the Step field IS NOT serializable and DOES implement Serializable (this can happen, we must unit test every step for serialization)
 *    3) for simple things (with a default constructor and no state), just re-initialize them in StepBase::reinitialize()
 *    4) for simple things that can be recreated with a serializable field, just re-initialize them in StepBase::reinitialize()
 *    5) for complex things (that maintain state), the methods below must be overwritten, and T must be converted into something serializable in writeObject,
 *    and then re-formed from that value in readObject. for example, write a data structure into a B64 String in writeObject() and re-create
 *    it from that String in readObject().
 * 6) create a new, serializable version of the object
 */
public interface StepFieldSerialization {

    void writeObject(ObjectOutputStream objectOutputStream) throws IOException;

    void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException;

}
