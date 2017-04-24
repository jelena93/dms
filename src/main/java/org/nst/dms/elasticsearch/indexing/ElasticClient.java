package org.nst.dms.elasticsearch.indexing;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Scope;

@Scope(scopeName = "singleton")
public class ElasticClient {

    private Client client;

    public ElasticClient() {
        Settings settings = Settings.builder().put("cluster.name", ElasticSearchUtil.CLUSTER_NAME).build();
        TransportClient transportClient = new PreBuiltTransportClient(settings);
        try {
            transportClient = transportClient
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ElasticSearchUtil.IP_ADDRESS), ElasticSearchUtil.PORT));
        } catch (UnknownHostException e) {
            return;
        }
        client = transportClient;
    }

    public Client getClient() {
        return client;
    }

}
