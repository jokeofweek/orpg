package orpg.shared.data.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Editable {
	String name();
	String description() default "";
}
