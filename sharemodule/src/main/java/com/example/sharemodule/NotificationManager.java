package com.example.sharemodule;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aishwarya on 23/6/16.
 */
public class NotificationManager {
    public interface Observer {
        void update(Notification notificationName, Bundle data);
    }

    private HashMap<Notification, ArrayList<Observer>> mObservablesMap = null;

    private NotificationManager() {
        mObservablesMap = new HashMap<Notification, ArrayList<Observer>>();
    }

    private static class NotificationManagerHolder {
        public static final NotificationManager mInstance = new NotificationManager();
    }

    /**
     * Returns an instance of the {@link NotificationManager}.
     *
     * @return
     */
    public static NotificationManager getInstance() {
        return NotificationManagerHolder.mInstance;
    }

    /**
     * Registers an Observer for the specified notification.
     *
     * @param notificationName
     * @param observer
     */
    public void addObserver(Notification notificationName, Observer observer) {
        if (observer == null) {
            throw new IllegalArgumentException("The observer is null.");
        }

        if (notificationName == null) {
            throw new IllegalArgumentException("The notificationName is null.");
        }

        ArrayList<Observer> observerList = null;
        synchronized (mObservablesMap) {
            if (mObservablesMap.containsKey(notificationName)) {
                observerList = mObservablesMap.get(notificationName);
                if (!observerList.contains(observer))
                    observerList.add(observer);
            } else {
                observerList = new ArrayList<Observer>();
                if (!observerList.contains(observer))
                    observerList.add(observer);
                mObservablesMap.put(notificationName, observerList);
            }
        }
    }

    /**
     * Unregisters the observer from a particular notification.
     *
     * @param notificationName
     * @param observer
     */
    public void removeObserver(Notification notificationName, Observer observer) {
        ArrayList<Observer> observerList = null;
        synchronized (mObservablesMap) {
            if (mObservablesMap.containsKey(notificationName)) {
                observerList = mObservablesMap.get(notificationName);

                //Remove the observer
                if (observerList.contains(observer))
                    observerList.remove(observer);

				/* Remove the key mapping if there are no other observers for the
                particular notification */
                if (observerList.isEmpty())
                    mObservablesMap.remove(notificationName);
            }
        }
    }

    /**
     * Notifies all the observers on occurance of an event.
     * Optional data can also be sent.
     *
     * @param notificationName
     * @param data
     */
    public void notifyObservers(Notification notificationName, Bundle data) {
        int size = 0;
        Observer[] observersToNotify = null;
        synchronized (mObservablesMap) {
            if (mObservablesMap.containsKey(notificationName)) {
                ArrayList<Observer> observers = mObservablesMap.get(notificationName);
                size = observers.size();
                observersToNotify = new Observer[size];
                observers.toArray(observersToNotify);
            }
        }

        //Notify all observers
        if (observersToNotify != null) {
            for (int i = 0; i < observersToNotify.length; i++) {
                observersToNotify[i].update(notificationName, data);
            }
        }

    }
}
