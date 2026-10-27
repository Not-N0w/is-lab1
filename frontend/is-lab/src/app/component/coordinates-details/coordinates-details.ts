import { Component, input } from '@angular/core';
import { Coordinates } from '../../model/Model';

@Component({
  selector: 'app-coordinates-details',
  templateUrl: './coordinates-details.html',
  styleUrl: '../details.scss',
})
export class CoordinatesDetails {
  value = input.required<Coordinates>();
}
