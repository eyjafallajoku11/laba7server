package utility;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Objects;

import static utility.Serialisation.deserialize;
import static utility.Serialisation.serialize;

public class CleintThread extends Thread{
    HashMap<String, String> users = new HashMap<>();
    private static SocketChannel channel = null;
    private static ByteBuffer[] bufferOut;

    private static ByteBuffer bufferIn;

    private static String answer;

    private static String login;
    private static String password;
    private static boolean authorized = false;
    public CleintThread(SocketChannel socketChannel) {
        channel = socketChannel;
    }

    @Override
    public void run() {
        while (channel.isConnected()) {
            try {
                Request request;
                int[] requestData = getRequestData();
                if (!Objects.isNull(requestData)) {
                    request = getRequest(requestData);
                    System.out.println(request);
                    if (request.getCommandName().equals("login")){
                        checkLogin();
                    }
                    else {
                        answer = CommandManager.execute(request);
                    }
                    sendAnswer(answer);
                }
            } catch (Exception e) {
                try {
                    channel.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        System.out.println("клиент отключен");
    }

    private void checkLogin() {
        Request request = getRequest(getRequestData());
        login = request.getCreatorArgument()[0];
        password = request.getCreatorArgument()[1];
        System.out.println("кринж");
        if ((users.containsKey(login) && (users.get(login).equals(password)))) {
            authorized = true;
            answer = "you're in";
        } else answer = "you're out";

    }

    private static void sendAnswer(String answer){
        int[] answerData = split(answer.getBytes());
        try {
            channel.write(ByteBuffer.wrap(serialize(answerData)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int size = answerData[1];
        try {
            for (int i=0; i<size; i++) {
                Thread.sleep(1);
                channel.write(bufferOut[i]);
                bufferOut[i].clear();
            }
        } catch (IOException e) {
            System.out.println("не отправляет");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private static Request getRequest(int[] bufferData) {
        Request request;
        bufferIn = ByteBuffer.allocate(bufferData[0]);
        int size = bufferData[1];
        byte[] input = new byte[0];
        try {
            for (int i=0; i < size; i++) {
                bufferIn.clear();
                int length;
                Thread.sleep(1);
                length = channel.read(bufferIn);
                input = combineArray(input, bufferIn.array(), length);
            }
            request = deserialize(input);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return request;
    }
    private static int[] getRequestData(){
        int[] data;
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        try {
            byteBuffer.clear();
            channel.read(byteBuffer);
            data = deserialize(byteBuffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
    private static byte[] combineArray(byte[] arr1, byte[] arr2, int length){
        byte[] arr = new byte[arr1.length+arr2.length];
        System.arraycopy(arr1, 0, arr, 0, arr1.length);
        System.arraycopy(arr2, 0, arr, arr1.length, length);
        return arr;
    }
    public static int[] split(byte[] dataArray) {
        int byteBufferSize = 1024;
        int size = (int) Math.ceil((double) dataArray.length / byteBufferSize);
        int stop = byteBufferSize;
        bufferOut = new ByteBuffer[size];
        for (int i = 0; i < size; i++){
            if (i+1 == size && dataArray.length % byteBufferSize != 0) {
                stop = (dataArray.length % byteBufferSize);
            }
            byte[] temp = new byte[stop];
            System.arraycopy(dataArray, i * byteBufferSize, temp, 0, stop);
            bufferOut[i] = ByteBuffer.wrap(temp);
        }
        return new int[] {byteBufferSize,size};
    }

}
