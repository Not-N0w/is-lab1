import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Button } from 'primeng/button';
import { LabWorkTable } from '../../component/lab-work-table/lab-work-table';
import { CoordinatesTable } from '../../component/coordinates-table/coordinates-table';
import { DisciplineTable } from '../../component/discipline-table/discipline-table';
import { PersonTable } from '../../component/person-table/person-table';
import { LocationTable } from '../../component/location-table/location-table';

@Component({
  selector: 'app-table-page',
  imports: [
    RouterLink,
    Button,
    LabWorkTable,
    CoordinatesTable,
    DisciplineTable,
    PersonTable,
    LocationTable,
  ],
  templateUrl: './table-page.html',
  styleUrl: './table-page.scss',
})
export class TablePage {
  entity = input.required<string>();
  title = input.required<string>();
}
