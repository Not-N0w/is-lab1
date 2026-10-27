import { Injectable } from '@angular/core';
import { RxStomp } from '@stomp/rx-stomp';
import { environment } from '../environment';

@Injectable({ providedIn: 'root' })
export class WebSocketService {
  private stomp = new RxStomp();

  constructor() {
    this.stomp.configure({ brokerURL: environment.wsUrl, reconnectDelay: 5000 });
    this.stomp.activate();
  }

  watch(type: string) {
    return this.stomp.watch('/topic/' + type);
  }
}
