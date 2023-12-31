package by.itacademy.profiler.usecasses.dto;

import by.itacademy.profiler.usecasses.annotation.DateBottomLimitValidation;
import by.itacademy.profiler.usecasses.annotation.IndustryValidation;
import by.itacademy.profiler.usecasses.util.Sequencable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.YearMonth;
import java.util.List;

import static by.itacademy.profiler.usecasses.util.ValidationConstants.REGEXP_VALIDATE_ACHIEVEMENTS;
import static by.itacademy.profiler.usecasses.util.ValidationConstants.REGEXP_VALIDATE_COMPANY_NAME;
import static by.itacademy.profiler.usecasses.util.ValidationConstants.REGEXP_VALIDATE_POSITION_AND_DUTIES;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Builder(setterPrefix = "with")
public record ExperienceRequestDto(
        @NotNull(message = "Sequence number must not be null")
        @Min(value = 1, message = "Sequence number must not be less than 1")
        @Max(value = 5, message = "Sequence number must not be more than 5")
        @Schema(defaultValue = "1", description = "Sequence number")
        Integer sequenceNumber,
        @PastOrPresent(message = "Date is in the future")
        @DateBottomLimitValidation(value = "1970-01")
        @NotNull(message = "Work period start must not be null")
        @Schema(defaultValue = "2020-02", description = "Working period from")
        YearMonth periodFrom,
        @PastOrPresent(message = "Date is in the future")
        @Schema(defaultValue = "2020-10", description = "Working period to")
        YearMonth periodTo,
        @NotNull(message = "Must be specified")
        @Schema(defaultValue = "false", description = "Is work up to now")
        Boolean presentTime,
        @NotNull(message = "Industry id must not be null")
        @IndustryValidation
        @Schema(defaultValue = "1", description = "Industry id")
        Long industryId,
        @NotNull(message = "Company name must not be null")
        @Pattern(regexp = REGEXP_VALIDATE_COMPANY_NAME, message = "Invalid company name")
        @Length(max = 40, message = "Company name is too long, the max number of symbols is 40")
        @Schema(defaultValue = "Company Name", description = "Company Name")
        String company,
        @NotNull(message = "Position must not be null")
        @Pattern(regexp = REGEXP_VALIDATE_POSITION_AND_DUTIES, message = "Invalid position name")
        @Length(max = 40, message = "Position name is too long, the max number of symbols is 40")
        @Schema(defaultValue = "Position Name", description = "Position Name")
        String position,
        @NotEmpty(message = "Duties must not be empty")
        @Schema(defaultValue = "[\"duty first\",\"duty second\"]", description = "List of duties")
        @Size(max = 5, message = "Amount of duties should not be more than 5")
        List<
                @NotNull(message = "Duty must not be null")
                @Length(max = 120, message = "Duty name is too long, the max number of symbols is 120")
                @Pattern(regexp = REGEXP_VALIDATE_POSITION_AND_DUTIES, message = "Invalid duty")
                        String> duties,
        @Length(max = 200, message = "Achievements name is too long, the max number of symbols is 200")
        @Pattern(regexp = REGEXP_VALIDATE_ACHIEVEMENTS, message = "Invalid achievements")
        @Schema(defaultValue = "I am the best of the best", description = "Achievements")
        String achievements,
        @Length(max = 255, message = "Link is too long, the max number of symbols is 255")
        @Schema(defaultValue = "http://example.com/link", description = "Link to a portfolio or project page")
        String link
) implements Serializable, Sequencable {

    private static final Integer MAX_DUTY_SIZE = 2;
    private static final Integer SEQUENCE_NUMBER_FROM = 2;
    private static final Integer SEQUENCE_NUMBER_TO = 6;

    @AssertTrue(message = "Field `periodTo` should be later than or equal to `periodFrom`")
    private boolean isPeriodToAfterOrEqualToPeriodFrom() {
        if (periodTo != null) {
            return periodFrom.isBefore(periodTo) || periodFrom.equals(periodTo);
        }
        return true;
    }

    @AssertTrue(message = "If field `presentTime` is true, then field `periodTo` should be null, otherwise should not be")
    private boolean isPresentTimeThenPeriodToNull() {
        if (Boolean.TRUE.equals(presentTime)) {
            return isNull(periodTo);
        } else {
            return nonNull(periodTo);
        }
    }

    @AssertTrue(message = "Amount of duties should not be more than 2 for 3-5 job`s positions in the list")
    private boolean isCorrectCountOfDuties() {
        if (sequenceNumber != null && (sequenceNumber > SEQUENCE_NUMBER_FROM && sequenceNumber < SEQUENCE_NUMBER_TO)) {
            return duties.size() <= MAX_DUTY_SIZE;
        }
        return true;
    }
}
