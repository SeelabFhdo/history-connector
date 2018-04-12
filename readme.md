<h1>HistoryService</h1>
<h2>Beispiel Thing Konfiguration </h2>
<p>
historyservice:HistoryService:history [ brokerurl="ssl://172.17.0.3:8883", instance="Openhab1", certpath="/home/phil/Dokumente/Development/Seelab/MQTT-Zertifikate/client.jks", certpassword="password" ]
</p>
<h2>Beispiel Konfiguration historyservice.persist Datei</h2>
<p>
Strategies {
    default = everyChange
}
Items {
    // log all temperatures on every change
    
    /* * : strategy=everyChange 
        Temperature* -> "temperatures" : strategy=everyChange    
    */
    TestSwitch : strategy=everyChange
    ZweiteSwitch : strategy=everyChange
}
</p>
