/* *******************************************
 * Copyright (c) 2011
 * HT srl,   All rights reserved.
 * Project      : RCS, AndroidService
 * File         : EventConnectivity.java
 * Created      : 6-mag-2011
 * Author		: zeno
 * *******************************************/

package com.android.service.event;

import java.io.IOException;

import com.android.service.Connectivity;
import com.android.service.auto.Cfg;
import com.android.service.interfaces.Observer;
import com.android.service.listener.ListenerConnectivity;
import com.android.service.util.Check;
import com.android.service.util.DataBuffer;

public class EventConnectivity extends EventBase implements Observer<Connectivity> {
		/** The Constant TAG. */
		private static final String TAG = "EventConnectivity";

		private int actionOnExit, actionOnEnter;
		private boolean inRange = false;
		
		@Override
		public void begin() {
			ListenerConnectivity.self().attach(this);
		}

		@Override
		public void end() {
			ListenerConnectivity.self().detach(this);
		}

		@Override
		public boolean parse(EventConf event) {
			super.setEvent(event);

			final byte[] conf = event.getParams();

			final DataBuffer databuffer = new DataBuffer(conf, 0, conf.length);
			
			try {
				actionOnEnter = event.getAction();
				actionOnExit = databuffer.readInt();
			} catch (final IOException e) {
				if(Cfg.DEBUG) Check.log( TAG + " Error: params FAILED");
				return false;
			}
			
			return true;
		}

		@Override
		public void go() {
			// TODO Auto-generated method stub
		}

		// Viene richiamata dal listener (dalla dispatch())
		public int notification(Connectivity c) {
			if(Cfg.DEBUG) Check.log( TAG + " Got connectivity status notification: " + c.isConnected());

			// Nel range
			if (c.isConnected() == true && inRange == false) {
				inRange = true;
				if(Cfg.DEBUG) Check.log( TAG + " Connectivity IN");
				onEnter();
			} else if (c.isConnected() == false && inRange == true) {
				inRange = false;
				if(Cfg.DEBUG) Check.log( TAG + " Connectivity OUT");
				onExit();
			}
			
			return 0;
		}
		
		public void onEnter() {
			trigger(actionOnEnter);
		}

		public void onExit() {
			trigger(actionOnExit);
		}
}