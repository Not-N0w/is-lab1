export type Color = 'RED' | 'BLUE' | 'ORANGE' | 'WHITE';
export type Country = 'USA' | 'SPAIN' | 'SOUTH_KOREA' | 'NORTH_KOREA';
export type Difficulty = 'VERY_EASY' | 'HARD' | 'TERRIBLE';
export type Strategy = 'delete' | 'pass';

export class Discipline {
  id?: number;
  name = '';
  practiceHours: number | null = null;
}

export class Coordinates {
  id?: number;
  x = 0;
  y = 0;
}

export class Location {
  id?: number;
  name: string | null = null;
  x = 0;
  y = 0;
  z = 0;
}

export class Person {
  id?: number;
  name = '';
  eyeColor: Color = 'RED';
  hairColor: Color | null = null;
  height = 0;
  nationality: Country | null = null;
  location = new Location();
}

export class LabWork {
  id?: number;
  name = '';
  coordinates = new Coordinates();
  creationDate = '';
  description: string | null = null;
  difficulty: Difficulty | null = null;
  discipline = new Discipline();
  minimalPoint = 0;
  tunedInWorks = 0;
  author: Person | null = null;
}

export interface LabWorkRequest {
  name: string;
  description: string | null;
  difficulty: Difficulty | null;
  minimalPoint: number;
  tunedInWorks: number;
  newCoordinates: boolean;
  coordinates: Coordinates | null;
  coordinatesId: number | null;
  newDiscipline: boolean;
  discipline: Discipline | null;
  disciplineId: number | null;
  newAuthor: boolean;
  author: Person | null;
  authorId: number | null;
}

export interface PersonRequest {
  name: string;
  eyeColor: Color;
  hairColor: Color | null;
  height: number;
  nationality: Country | null;
  newLocation: boolean;
  location: Location | null;
  locationId: number | null;
}

export interface AuthRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
}
export function toLabWorkRequest(lab: LabWork): LabWorkRequest {
  const newCoordinates = lab.coordinates.id == null;
  const newDiscipline = lab.discipline.id == null;
  const newAuthor = lab.author != null && lab.author.id == null;

  return {
    name: lab.name,
    description: lab.description,
    difficulty: lab.difficulty,
    minimalPoint: lab.minimalPoint,
    tunedInWorks: lab.tunedInWorks,
    newCoordinates,
    coordinates: newCoordinates ? lab.coordinates : null,
    coordinatesId: newCoordinates ? null : lab.coordinates.id!,
    newDiscipline,
    discipline: newDiscipline ? lab.discipline : null,
    disciplineId: newDiscipline ? null : lab.discipline.id!,
    newAuthor,
    author: newAuthor ? lab.author : null,
    authorId: newAuthor ? null : (lab.author?.id ?? null),
  };
}

export function toPersonRequest(person: Person): PersonRequest {
  const newLocation = person.location.id == null;

  return {
    name: person.name,
    eyeColor: person.eyeColor,
    hairColor: person.hairColor,
    height: person.height,
    nationality: person.nationality,
    newLocation,
    location: newLocation ? person.location : null,
    locationId: newLocation ? null : person.location.id!,
  };
}
