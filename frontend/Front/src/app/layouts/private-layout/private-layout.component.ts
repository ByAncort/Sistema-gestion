import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {SlideComponent} from '../../shared/slide/slide.component';

@Component({
  selector: 'app-private-layout',
  standalone: true,
  imports: [RouterOutlet, SlideComponent],
  templateUrl: './private-layout.component.html',
  styleUrls: ['./private-layout.component.css']
})
export class PrivateLayoutComponent {
  isOpen = false;
}
