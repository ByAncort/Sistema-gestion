import { CommonModule } from '@angular/common'; // <-- Añade esta importación
import { Component, HostListener, ViewChild, ElementRef } from '@angular/core';
import {RouterModule} from '@angular/router';

interface MenuItem {
  id: string;
  nombre: string;
  ruta?: string;
  icono: string;
  submenu?: SubMenuItem[];
}

interface SubMenuItem {
  nombre: string;
  ruta: string;
}

@Component({
  selector: 'app-slide',
  standalone: true,
  imports: [CommonModule,RouterModule],
  templateUrl: './slide.component.html',
  styleUrls: ['./slide.component.css']
})
export class SlideComponent {
  isMinimized = false;

  menuItems: MenuItem[] = [
    {
      id: 'dashboard',
      nombre: 'Dashboard',
      icono: 'M7.5 14.25v2.25m3-4.5v4.5m3-6.75v6.75m3-9v9M6 20.25h12A2.25 2.25 0 0 0 20.25 18V6A2.25 2.25 0 0 0 18 3.75H6A2.25 2.25 0 0 0 3.75 6v12A2.25 2.25 0 0 0 6 20.25Z',
      submenu: [
        {
          nombre: 'Statistics',
          ruta: '/statistics'
        },
        {
          nombre: 'Reports',
          ruta: '/reports'
        }
      ]
    },
    {
      id: 'team',
      nombre: 'Team',
      ruta: '/team',
      icono: 'M10 15 a3 3 0 1 0 6 0 a3 3 0 1 0 -6 0 M25 15 a3 3 0 1 0 6 0 a3 3 0 1 0 -6 0 M17.5 25 a3 3 0 1 0 6 0 a3 3 0 1 0 -6 0 M10 20 v5 M25 20 v5 M20 20 v5 M12.5 17.5 L17.5 22.5 M22.5 22.5 L27.5 17.5'
    },
    {
      id: 'all-workspaces',
      nombre: 'workspaces',
      ruta: '/all-workspaces',
      icono: 'M10 15 a3 3 0 1 0 6 0 a3 3 0 1 0 -6 0 M25 15 a3 3 0 1 0 6 0 a3 3 0 1 0 -6 0 M17.5 25 a3 3 0 1 0 6 0 a3 3 0 1 0 -6 0 M10 20 v5 M25 20 v5 M20 20 v5 M12.5 17.5 L17.5 22.5 M22.5 22.5 L27.5 17.5'
    }

  ];

  hoveredItem: string | null = null;
  submenuStates: { [key: string]: boolean } = {};

  toggleSubmenu(menuId: string) {
    if (this.isMinimized) {
      return;
    }
    this.submenuStates[menuId] = !this.submenuStates[menuId];
  }

  toggleMenu() {
    this.hoveredItem = null;

    if (!this.isMinimized) {
      this.submenuStates = {};
    }

    this.isMinimized = !this.isMinimized;
  }


}
