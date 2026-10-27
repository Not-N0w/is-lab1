import { Component, input } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Tag } from 'primeng/tag';
import { LabWork } from '../../model/Model';
import { CoordinatesDetails } from '../coordinates-details/coordinates-details';
import { DisciplineDetails } from '../discipline-details/discipline-details';
import { PersonDetails } from '../person-details/person-details';

@Component({
  selector: 'app-lab-work-details',
  imports: [DatePipe, RouterLink, Tag, CoordinatesDetails, DisciplineDetails, PersonDetails],
  templateUrl: './lab-work-details.html',
  styleUrl: '../details.scss',
})
export class LabWorkDetails {
  value = input.required<LabWork>();

  protected severity(difficulty: string) {
    switch (difficulty) {
      case 'VERY_EASY':
        return 'success';
      case 'HARD':
        return 'warn';
      case 'TERRIBLE':
        return 'danger';
      default:
        return 'info';
    }
  }
}
