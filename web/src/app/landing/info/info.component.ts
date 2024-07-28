import { Component } from '@angular/core';

import { MatCardModule } from '@angular/material/card';
import { ItemComponent } from './item/item.component';

@Component({
  selector: 'app-info',
  standalone: true,
  imports: [MatCardModule, InfoComponent, ItemComponent],
  templateUrl: './info.component.html',
  styleUrl: './info.component.scss',
})
export class InfoComponent {}
