import { Component, input } from '@angular/core';
import { Discipline } from '../../model/Model';

@Component({
  selector: 'app-discipline-details',
  templateUrl: './discipline-details.html',
  styleUrl: '../details.scss',
})
export class DisciplineDetails {
  value = input.required<Discipline>();
}
