package org.openhab.binding.historyservice.internal;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Locale;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.items.ItemRegistry;
import org.eclipse.smarthome.core.persistence.PersistenceService;
import org.openhab.binding.historyservice.historyserviceBindingConstants;
import org.openhab.binding.historyservice.internal.entities.ItemState;
import org.openhab.binding.historyservice.internal.entities.TransferItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * This call is the main class of the HistoryServiceBinding.
 * It implements the interface PersistenceService.
 */
public class HistoryService implements PersistenceService {
    private Logger logger = LoggerFactory.getLogger(HistoryService.class);
    private MqttClient mqttClient;
    private MqttConnectOptions mqttConnectOptions;

    private ItemRegistry itemRegistry;

    /**
     * This method is called, when the binding is started.
     */
    public void activate() {
        logger.info("Push Service activate.");
    }

    /**
     * This method is called, when the binding is stopped.
     */
    public void deactivate() {
        logger.info("Push Service deactivate.");
    }

    /**
     * This method sets the ItemRegistory for the binding.
     *
     * @param itemRegistry which contains all Openhab2 Items
     */
    public void setItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry = itemRegistry;
    }

    /**
     * This method unsets the ItemRegistory for the Binding.
     *
     * @param itemRegistry which contains all Openhab2 Items
     */
    public void unsetItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry = null;
    }

    /**
     * This method returns the name of the .persist file, which contains all items
     * that will be stored.
     *
     * @return String name of the .persist file
     */

    public String getName() {
        return "historyservice";
    }

    /**
     * This method is called, when an Itemstate has changed.
     * It implement the function that sends the changed Item to the MQTTBrocker
     */
    @Override
    public void store(Item openHabItem) {

        if (openHabItem.getState() == null) {
            return;
        }

        if (mqttClient == null) {
            createMqttClient();
        }

        if (!mqttClient.isConnected()) {
            connectToBroker();
        }

        ItemState itemState = new ItemState(openHabItem.getState().toString());

        TransferItem transferItem = new TransferItem();
        transferItem.setItemName(openHabItem.getName());
        transferItem.setItemType(openHabItem.getType());
        transferItem.setItemState(itemState);

        String historyItemJson = new Gson().toJson(transferItem);
        this.sendMessage(historyItemJson);
    }

    /**
     * This method is called, when an ItemState has changed and the Item has an alias name.
     */
    @Override
    public void store(Item item, String alias) {
        store(item);
    }

    /**
     * This method creates the mqttClient, with the parameters from the thing configuration file.
     */
    private void createMqttClient() {
        try {
            mqttClient = new MqttClient(historyserviceBindingConstants.VALUE_BROKER_URL,
                    historyserviceBindingConstants.VALUE_INSTANCE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This mathod connects the mqttClient to the MQTTBrocker.
     */
    private void connectToBroker() {
        try {
            if (mqttConnectOptions == null) {
                createMqttConnectOptions();
            }

            mqttClient.connect(mqttConnectOptions);
        } catch (MqttSecurityException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method sets the mqttConnectOptions for the MqttClient.
     * All parameters are loaded from the thing configuration file.
     */
    private void createMqttConnectOptions() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(historyserviceBindingConstants.VALUE_CERTPATH),
                    historyserviceBindingConstants.VALUE_CERTPASSWORD.toCharArray());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setSocketFactory(sslContext.getSocketFactory());
            mqttConnectOptions.setUserName(historyserviceBindingConstants.VALUE_USERNAME);
            mqttConnectOptions.setPassword(historyserviceBindingConstants.VALUE_PASSWORD.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method send the MqttMessage to the MqttBrocker.
     *
     * @param textMessage Json String, which is send to the MqttBrocker.
     */
    public void sendMessage(String textMessage) {
        logger.debug(textMessage);
        MqttMessage message = new MqttMessage(textMessage.getBytes());
        message.setQos(2);
        message.setRetained(false);
        MqttDeliveryToken mqttDeliveryToken = null;
        MqttTopic mqttTopic = mqttClient.getTopic("historyservice/" + historyserviceBindingConstants.VALUE_INSTANCE);

        try {
            mqttDeliveryToken = mqttTopic.publish(message);
            mqttDeliveryToken.waitForCompletion();

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getId() {
        return "historyservice";
    }

    @Override
    public String getLabel(Locale locale) {
        return "historyservice";
    }

}
