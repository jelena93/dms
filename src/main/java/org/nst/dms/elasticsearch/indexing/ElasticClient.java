package org.nst.dms.elasticsearch.indexing;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Scope;

@Scope(scopeName = "singleton")
public class ElasticClient {

    final static Logger log = LogManager.getLogger(ElasticClient.class);
    private Client client;
    private final String clusterName = "elasticsearch";
    private final String ipAddress = "127.0.0.1";
    private final int port = 9300;

    public ElasticClient() {
        log.info("@@@@@@@@@@@@@ kreira se ElasticClient");
        Settings settings = Settings.builder().put("cluster.name", clusterName).build();
        TransportClient transportClient = new PreBuiltTransportClient(settings);
        try {
            transportClient = transportClient
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ipAddress), port));
        } catch (UnknownHostException e) {
            return;
        }
        client = transportClient;
    }

    public Client getClient() {
        return client;
    }

}
