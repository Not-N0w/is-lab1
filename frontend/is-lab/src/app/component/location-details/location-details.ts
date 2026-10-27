import { Component, input } from '@angular/core';
import { Location } from '../../model/Model';

@Component({
  selector: 'app-location-details',
  templateUrl: './location-details.html',
  styleUrl: '../details.scss',
})
export class LocationDetails {
  value = input.required<Location>();
}
