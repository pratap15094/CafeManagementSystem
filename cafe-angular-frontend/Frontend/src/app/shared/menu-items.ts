import { Injectable } from "@angular/core";
import { type } from "os";

export interface Menu {
    state: string;
    name: string;
    type: string;
    icon: string;
    role: string;
}
const MESUREMENTS = [
    { state: 'dashboard', name: 'Dashboard', type: 'link', icon: 'dashboard', role: '' },
    { state: 'category', name: 'Manged Category', type: 'link', icon: 'category', role: 'admin' }
]

@Injectable()
export class MenuItems {
    getMenuItem(): Menu[] {
        return MESUREMENTS;
    }
}