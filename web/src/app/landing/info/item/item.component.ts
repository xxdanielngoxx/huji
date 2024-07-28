import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-info-item',
  standalone: true,
  imports: [],
  templateUrl: './item.component.html',
  styleUrl: './item.component.scss',
})
export class ItemComponent {
  @Input() propertyName = '';
  @Input() value = '';
}
