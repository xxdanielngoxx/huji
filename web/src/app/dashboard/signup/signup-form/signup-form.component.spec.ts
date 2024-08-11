import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SignupFormComponent } from './signup-form.component';
import { provideHttpClient } from '@angular/common/http';
import { RegisterOwnerService } from '../shared/service/register-owner.service';
import { provideRouter } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

describe('SignupFormComponent', () => {
  let component: SignupFormComponent;
  let fixture: ComponentFixture<SignupFormComponent>;

  const registerOwnerServiceMock = {
    registerOwner: jest.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SignupFormComponent],
      providers: [
        provideHttpClient(),
        provideRouter([]),
        provideAnimationsAsync(),
        {
          provide: RegisterOwnerService,
          useValue: registerOwnerServiceMock,
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SignupFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
