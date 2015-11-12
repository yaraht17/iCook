package com.infinity.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.infinity.adapter.ChatAdapter;
import com.infinity.icook.R;
import com.infinity.model.ChatMessage;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    //chat view
    private ChatAdapter chatAdapter;
    private ListView chatListView;
    private ArrayList<ChatMessage> chatList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_view, container, false);

        chatListView = (ListView) view.findViewById(R.id.messagesContainer);

        chatAdapter = new ChatAdapter(getActivity().getApplicationContext(), -1, chatList);
        chatListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        chatListView.setAdapter(chatAdapter);

        return view;
    }

    public void sendChatMessage(ChatMessage message) {
        chatAdapter.add(message);
    }


}
