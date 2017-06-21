package com.talesdev.judge.task;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.talesdev.judge.Server;
import com.talesdev.judge.program.StreamOutputCollection;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * Judge response
 */
public class JudgeResponse {
    private String user;
    private int id;
    private String status;
    private String errorMessage;
    private String compilerMessage;
    private String result;
    private int passed;
    private int total;
    private int time;
    private int memory;
    @SerializedName("api_token")
    private String apiToken;
    private final transient Server server;

    public JudgeResponse(Server server, String user, int id, String apiToken, String status,
                         String errorMessage, String compilerMessage, String result,
                         int passed, int total, int time, int memory
    ) {
        this.server = server;
        this.user = user;
        this.id = id;
        this.status = status;
        this.compilerMessage = compilerMessage;
        this.errorMessage = errorMessage;
        this.result = result;
        this.passed = passed;
        this.total = total;
        this.time = time;
        this.memory = memory;
        this.apiToken = apiToken;
    }

    public JudgeResponse(Server server, String user,int id,String apiToken, String status ){
        this(server,user,id,apiToken,status,null, "", null,0,0,0,0);
    }

    public JudgeResponse(Server server, String user,int id,String apiToken, String status ,String errorMessage, String compilerMessage){
        this(server,user,id,apiToken,status, errorMessage, compilerMessage, null,0,0,0,0);
    }


    public void send() {
        // JSON String
        Gson gson = new Gson();
        String json = gson.toJson(this, JudgeResponse.class);
        // Http Request
        String url = "http://" + server.config().getNotifyURL() + "/result/";
        if(status != null && status.isEmpty())
            url += "submit";
        else
            url += "progress";
        StringEntity entity = new StringEntity(json,"UTF-8");
        entity.setContentType("application/json");
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.setHeader("Accept","application/json");
        post.setEntity(entity);
        try {
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() != 200){
                System.out.println("[JudgeResponse] Warning! server does not return 200 OK");
            }
            String output = new StreamOutputCollection(response.getEntity().getContent()).asString();
            if(output.length() > 110)
                output = output.substring(0,100) + "...(total:" + output.length();
            System.out.println("[JudgeResponse] Output from main server: " + output);
        } catch (IOException e) {
            System.out.println("[JudgeResponse] Failed to send notification to main server");
            e.printStackTrace();
            System.out.println(json);
        }
    }
}
