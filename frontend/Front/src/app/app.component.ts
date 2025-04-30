import {Component, inject} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {AuthService} from './auth.service';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [CommonModule, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

}
