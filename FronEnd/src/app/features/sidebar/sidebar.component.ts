import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit, OnDestroy {
  isSidebarOpen = true;
  isExpanded = false;
  isMobile = false;
  private mobileBreakpoint = 1024;

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.checkMobile();
    this.loadSavedState();
    this.setupRouterEvents();
    window.addEventListener('resize', this.checkMobile.bind(this));
  }

  ngOnDestroy(): void {
    window.removeEventListener('resize', this.checkMobile.bind(this));
  }

  @HostListener('window:resize', ['$event'])
  onResize() {
    this.checkMobile();
  }

  private checkMobile(): void {
    this.isMobile = window.innerWidth < this.mobileBreakpoint;
    if (this.isMobile && this.isSidebarOpen) {
      this.isSidebarOpen = false;
    }
  }

  private loadSavedState(): void {
    if (!this.isMobile) {
      const savedState = localStorage.getItem('sidebarState');
      this.isSidebarOpen = savedState ? JSON.parse(savedState) : true;
    }
  }

  private setupRouterEvents(): void {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        if (this.isMobile) {
          this.isSidebarOpen = false;
        }
      });
  }

  toggleSidebar(): void {
    this.isSidebarOpen = !this.isSidebarOpen;
    this.isExpanded = false;

    if (!this.isMobile) {
      localStorage.setItem('sidebarState', JSON.stringify(this.isSidebarOpen));
    }
  }

  toggleMenu(event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    if (this.isSidebarOpen) {
      this.isExpanded = !this.isExpanded;
    }
  }

  // Para ocultar el sidebar en mobile al hacer clic fuera
  handleOverlayClick(event: Event): void {
    if (this.isMobile && this.isSidebarOpen) {
      this.toggleSidebar();
    }
  }
}
