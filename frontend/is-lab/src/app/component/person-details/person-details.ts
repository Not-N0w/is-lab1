import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Person } from '../../model/Model';
import { LocationDetails } from '../location-details/location-details';

@Component({
  selector: 'app-person-details',
  imports: [RouterLink, LocationDetails],
  templateUrl: './person-details.html',
  styleUrl: '../details.scss',
})
export class PersonDetails {
  value = input.required<Person>();
}
