package com.betting.entry;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.UUID;

@Builder
record EventOutcomeRequest(@NotNull @UUID String eventId, @NotNull @UUID String eventWinnerId) {}
