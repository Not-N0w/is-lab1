import { Component, inject, signal } from '@angular/core';
import {
  MinimalPointCount,
  SpecialOperationsService,
} from '../../service/SpecialOperationsService';
import { Button } from 'primeng/button';
import { FormsModule } from '@angular/forms';

type Status = { text: string; ok: boolean };
import { NgTemplateOutlet } from '@angular/common';
import { WebSocketService } from '../../service/WebSocketService';


@Component({
  selector: 'app-special-functions-page',
  imports: [Button, FormsModule, NgTemplateOutlet],
  templateUrl: './special-functions-page.html',
  styleUrl: './special-functions-page.scss',
})
export class SpecialFunctionsPage {
  private service = inject(SpecialOperationsService);

  protected sum = signal<number | null>(null);
  protected groups = signal<MinimalPointCount[] | null>(null);
  protected authors = signal<number[] | null>(null);
  protected status = signal<Record<string, Status>>({});

  protected labId: number | null = null;
  protected disciplineId: number | null = null;
  protected topDisciplineId: number | null = null;

  loadSum() {
    this.service.getMinimalPointSum().subscribe({
      next: (v) => {
        this.sum.set(v);
        this.clear('sum');
      },
      error: (e) => this.fail('sum', e),
    });
  }

  loadGroups() {
    this.service.countByMinimalPoint().subscribe({
      next: (v) => {
        this.groups.set(v);
        this.clear('groups');
      },
      error: (e) => this.fail('groups', e),
    });
  }

  loadAuthors() {
    this.service.getUniqueAuthors().subscribe({
      next: (v) => {
        this.authors.set(v);
        this.clear('authors');
      },
      error: (e) => this.fail('authors', e),
    });
  }

  addLab() {
    this.service.addLabToDiscipline(this.labId!, this.disciplineId!).subscribe({
      next: () =>
        this.done(
          'addLab',
          `Лабораторная ${this.labId} добавлена в дисциплину ${this.disciplineId}`,
        ),
      error: (e) => this.fail('addLab', e),
    });
  }

  addTop() {
    this.service.addTopLabsToDiscipline(this.topDisciplineId!).subscribe({
      next: () =>
        this.done(
          'addTop',
          `10 самых сложных лабораторных добавлены в дисциплину ${this.topDisciplineId}`,
        ),
      error: (e) => this.fail('addTop', e),
    });
  }

  private done(key: string, text: string) {
    this.status.update((s) => ({ ...s, [key]: { text, ok: true } }));
  }

  private fail(key: string, e: any) {
    this.status.update((s) => ({
      ...s,
      [key]: { text: e.error?.message ?? 'Ошибка запроса', ok: false },
    }));
  }

  private clear(key: string) {
    this.status.update(({ [key]: _, ...rest }) => rest);
  }
}
