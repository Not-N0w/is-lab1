import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function greaterThan(min: number): ValidatorFn {
  return (c: AbstractControl): ValidationErrors | null =>
    c.value === null || c.value === '' || c.value > min ? null : { greaterThan: min };
}

export function notBlank(c: AbstractControl): ValidationErrors | null {
  return typeof c.value === 'string' && c.value.trim() === '' ? { blank: true } : null;
}

export const integer: ValidatorFn = (c: AbstractControl): ValidationErrors | null =>
  c.value === null || c.value === '' || Number.isInteger(c.value) ? null : { integer: true };
