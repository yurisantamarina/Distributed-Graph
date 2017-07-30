package br.ufu.core;

import io.atomix.catalyst.transport.Address;
import io.atomix.copycat.server.CopycatServer;
import io.atomix.copycat.server.storage.Storage;
import io.atomix.copycat.server.storage.StorageLevel;

public class ServerCopycat {

	public static void main(String args[]) {
		CopycatServer copycatServer;

		String ipLider, ipLocal;
		int portaLider, portaLocal;

		ipLider = args[0];
		portaLider = Integer.parseInt(args[1]);

		ipLocal = args[2];
		portaLocal = Integer.parseInt(args[3]);

		copycatServer = CopycatServer
				.builder(new Address(ipLocal, portaLocal))
				.withStorage(
						Storage.builder().withStorageLevel(StorageLevel.MEMORY)
								.build())
				.withStateMachine(MapStateMachine::new).build();

		if (portaLider == portaLocal) {
			copycatServer.bootstrap().join();
			System.out.printf("LIDER PORTA = %d\n", portaLocal);
		} else {
			copycatServer.join(new Address(ipLider, portaLider)).join();
			System.out.printf("FOLLOWER PORTA = %d\n", portaLocal);
		}

	}
}
